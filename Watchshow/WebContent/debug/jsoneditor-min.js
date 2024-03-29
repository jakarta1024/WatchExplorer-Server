if (!Array.prototype.indexOf) {
	Array.prototype.indexOf = function(b) {
		for ( var a = 0; a < this.length; a++) {
			if (this[a] == b) {
				return a
			}
		}
		return -1
	}
}
if (!Array.prototype.forEach) {
	Array.prototype.forEach = function(d, c) {
		for ( var b = 0, a = this.length; b < a; ++b) {
			d.call(c || this, this[b], b, this)
		}
	}
}
var JSON;
JSONEditor = function(a, b, c) {
	if (!JSON) {
		throw new Error(
				"Your browser does not support JSON. \n\nPlease install the newest version of your browser.\n(all modern browsers support JSON).")
	}
	if (!a) {
		throw new Error("No container element provided.")
	}
	this.container = a;
	this.dom = {};
	this._setOptions(b);
	if (this.options.history) {
		this.history = new JSONEditor.History(this)
	}
	this._createFrame();
	this._createTable();
	this.set(c || {})
};
JSONEditor.prototype._setOptions = function(a) {
	this.options = {
		search : true,
		history : true
	};
	if (a) {
		for ( var b in a) {
			if (a.hasOwnProperty(b)) {
				this.options[b] = a[b]
			}
		}
		if (a.enableSearch) {
			this.options.search = a.enableSearch;
			console
					.log('WARNING: Option "enableSearch" is deprecated. Use "search" instead.')
		}
		if (a.enableHistory) {
			this.options.search = a.enableHistory;
			console
					.log('WARNING: Option "enableHistory" is deprecated. Use "history" instead.')
		}
	}
};
JSONEditor.focusNode = undefined;
JSONEditor.prototype.set = function(a) {
	if (a instanceof Function || (a === undefined)) {
		this.clear()
	} else {
		this.content.removeChild(this.table);
		var d = {
			value : a
		};
		var c = new JSONEditor.Node(d);
		this._setRoot(c);
		var b = false;
		this.node.expand(b);
		this.content.appendChild(this.table)
	}
	if (this.history) {
		this.history.clear()
	}
};
JSONEditor.prototype.get = function() {
	if (JSONEditor.focusNode) {
		JSONEditor.focusNode.blur()
	}
	if (this.node) {
		return this.node.getValue()
	} else {
		return undefined
	}
};
JSONEditor.prototype.clear = function() {
	if (this.node) {
		this.node.collapse();
		this.tbody.removeChild(this.node.getDom());
		delete this.node
	}
};
JSONEditor.prototype._setRoot = function(b) {
	this.clear();
	this.node = b;
	var a = this;
	b.getEditor = function() {
		return a
	};
	this.tbody.appendChild(b.getDom())
};
JSONEditor.prototype.search = function(b) {
	var a;
	if (this.node) {
		this.content.removeChild(this.table);
		a = this.node.search(b);
		this.content.appendChild(this.table)
	} else {
		a = []
	}
	return a
};
JSONEditor.prototype.expandAll = function() {
	if (this.node) {
		this.content.removeChild(this.table);
		this.node.expand();
		this.content.appendChild(this.table)
	}
};
JSONEditor.prototype.collapseAll = function() {
	if (this.node) {
		this.content.removeChild(this.table);
		this.node.collapse();
		this.content.appendChild(this.table)
	}
};
JSONEditor.prototype.onAction = function(b, c) {
	if (this.history) {
		this.history.add(b, c)
	}
	if (this.options.change) {
		try {
			this.options.change()
		} catch (a) {
			console.log("Error in change callback: ", a)
		}
	}
};
JSONEditor.prototype.focus = function() {
};
JSONEditor.prototype.scrollTo = function(g) {
	var f = this.content;
	if (f) {
		var e = this;
		if (e.animateTimeout) {
			clearTimeout(e.animateTimeout);
			delete e.animateTimeout
		}
		var a = f.clientHeight;
		var d = f.scrollHeight - a;
		var b = Math.min(Math.max(g - a / 4, 0), d);
		var c = function() {
			var i = f.scrollTop;
			var h = (b - i);
			if (Math.abs(h) > 3) {
				f.scrollTop += h / 3;
				e.animateTimeout = setTimeout(c, 50)
			}
		};
		c()
	}
};
JSONEditor.History = function(a) {
	this.editor = a;
	this.clear();
	this.actions = {
		editField : {
			undo : function(b) {
				b.params.node.updateField(b.params.oldValue)
			},
			redo : function(b) {
				b.params.node.updateField(b.params.newValue)
			}
		},
		editValue : {
			undo : function(b) {
				b.params.node.updateValue(b.params.oldValue)
			},
			redo : function(b) {
				b.params.node.updateValue(b.params.newValue)
			}
		},
		appendNode : {
			undo : function(b) {
				b.params.parent.removeChild(b.params.node)
			},
			redo : function(b) {
				b.params.parent.appendChild(b.params.node)
			}
		},
		removeNode : {
			undo : function(d) {
				var c = d.params.parent;
				var b = c.childs[d.params.index] || c.append;
				c.insertBefore(d.params.node, b)
			},
			redo : function(b) {
				b.params.parent.removeChild(b.params.node)
			}
		},
		duplicateNode : {
			undo : function(b) {
				b.params.parent.removeChild(b.params.clone)
			},
			redo : function(b) {
				b.params.parent.insertBefore(b.params.clone, b.params.node)
			}
		},
		changeType : {
			undo : function(b) {
				b.params.node.changeType(b.params.oldType)
			},
			redo : function(b) {
				b.params.node.changeType(b.params.newType)
			}
		},
		moveNode : {
			undo : function(b) {
				b.params.startParent.moveTo(b.params.node, b.params.startIndex)
			},
			redo : function(b) {
				b.params.endParent.moveTo(b.params.node, b.params.endIndex)
			}
		}
	}
};
JSONEditor.History.prototype.onChange = function() {
};
JSONEditor.History.prototype.add = function(a, b) {
	this.index++;
	this.history[this.index] = {
		action : a,
		params : b,
		timestamp : new Date()
	};
	if (this.index < this.history.length - 1) {
		this.history.splice(this.index + 1, this.history.length - this.index
				- 1)
	}
	this.onChange()
};
JSONEditor.History.prototype.clear = function() {
	this.history = [];
	this.index = -1;
	this.onChange()
};
JSONEditor.History.prototype.canUndo = function() {
	return (this.index >= 0)
};
JSONEditor.History.prototype.canRedo = function() {
	return (this.index < this.history.length - 1)
};
JSONEditor.History.prototype.undo = function() {
	if (this.canUndo()) {
		var b = this.history[this.index];
		if (b) {
			var a = this.actions[b.action];
			if (a && a.undo) {
				a.undo(b)
			} else {
				console.log('Error: unknown action "' + b.action + '"')
			}
		}
		this.index--;
		this.onChange()
	}
};
JSONEditor.History.prototype.redo = function() {
	if (this.canRedo()) {
		this.index++;
		var b = this.history[this.index];
		if (b) {
			if (b) {
				var a = this.actions[b.action];
				if (a && a.redo) {
					a.redo(b)
				} else {
					console.log('Error: unknown action "' + b.action + '"')
				}
			}
		}
		this.onChange()
	}
};
JSONEditor.Node = function(a) {
	this.dom = {};
	this.expanded = false;
	if (a && (a instanceof Object)) {
		this.setField(a.field, a.fieldEditable);
		this.setValue(a.value)
	} else {
		this.setField();
		this.setValue()
	}
};
JSONEditor.Node.prototype.setParent = function(a) {
	this.parent = a
};
JSONEditor.Node.prototype.getParent = function() {
	return this.parent
};
JSONEditor.Node.prototype.getEditor = function() {
	return this.parent ? this.parent.getEditor() : undefined
};
JSONEditor.Node.prototype.setField = function(b, a) {
	this.field = b;
	this.fieldEditable = (a == true)
};
JSONEditor.Node.prototype.getField = function() {
	if (this.field === undefined) {
		this._getDomField()
	}
	return this.field
};
JSONEditor.Node.prototype.setValue = function(f) {
	var b, g;
	var e = this.childs;
	if (e) {
		while (e.length) {
			this.removeChild(e[0])
		}
	}
	this.type = this._getType(f);
	if (this.type == "array") {
		this.childs = [];
		for ( var d = 0, a = f.length; d < a; d++) {
			b = f[d];
			if (b !== undefined && !(b instanceof Function)) {
				g = new JSONEditor.Node({
					value : b
				});
				this.appendChild(g)
			}
		}
		this.value = ""
	} else {
		if (this.type == "object") {
			this.childs = [];
			for ( var c in f) {
				if (f.hasOwnProperty(c)) {
					b = f[c];
					if (b !== undefined && !(b instanceof Function)) {
						g = new JSONEditor.Node({
							field : c,
							value : b
						});
						this.appendChild(g)
					}
				}
			}
			this.value = ""
		} else {
			this.childs = undefined;
			this.value = f
		}
	}
};
JSONEditor.Node.prototype.getValue = function() {
	if (this.type == "array") {
		var a = [];
		this.childs.forEach(function(c) {
			a.push(c.getValue())
		});
		return a
	} else {
		if (this.type == "object") {
			var b = {};
			this.childs.forEach(function(c) {
				b[c.getField()] = c.getValue()
			});
			return b
		} else {
			if (this.value === undefined) {
				this._getDomValue()
			}
			return this.value
		}
	}
};
JSONEditor.Node.prototype.getLevel = function() {
	return (this.parent ? this.parent.getLevel() + 1 : 0)
};
JSONEditor.Node.prototype.clone = function() {
	var b = new JSONEditor.Node();
	b.type = this.type;
	b.field = this.field;
	b.fieldInnerText = this.fieldInnerText;
	b.fieldEditable = this.fieldEditable;
	b.value = this.value;
	b.valueInnerText = this.valueInnerText;
	b.expanded = this.expanded;
	if (this.childs) {
		var a = [];
		this.childs.forEach(function(d) {
			var c = d.clone();
			c.setParent(b);
			a.push(c)
		});
		b.childs = a
	} else {
		b.childs = undefined
	}
	return b
};
JSONEditor.Node.prototype.expand = function(a) {
	if (!this.childs) {
		return
	}
	this.expanded = true;
	if (this.dom.expand) {
		this.dom.expand.className = "jsoneditor-expanded"
	}
	this.showChilds();
	if (a != false) {
		this.childs.forEach(function(b) {
			b.expand(a)
		})
	}
};
JSONEditor.Node.prototype.collapse = function(a) {
	if (!this.childs) {
		return
	}
	this.hideChilds();
	if (a != false) {
		this.childs.forEach(function(b) {
			b.collapse(a)
		})
	}
	if (this.dom.expand) {
		this.dom.expand.className = "jsoneditor-collapsed"
	}
	this.expanded = false
};
JSONEditor.Node.prototype.showChilds = function() {
	var e = this.childs;
	if (!e) {
		return
	}
	if (!this.expanded) {
		return
	}
	var d = this.dom.tr;
	var c = d ? d.parentNode : undefined;
	if (c) {
		var a = this.getAppend();
		var b = d.nextSibling;
		if (b) {
			c.insertBefore(a, b)
		} else {
			c.appendChild(a)
		}
		this.childs.forEach(function(f) {
			c.insertBefore(f.getDom(), a);
			f.showChilds()
		})
	}
};
JSONEditor.Node.prototype.hide = function() {
	var b = this.dom.tr;
	var a = b ? b.parentNode : undefined;
	if (a) {
		a.removeChild(b)
	}
	this.hideChilds()
};
JSONEditor.Node.prototype.hideChilds = function() {
	var b = this.childs;
	if (!b) {
		return
	}
	if (!this.expanded) {
		return
	}
	var a = this.getAppend();
	if (a.parentNode) {
		a.parentNode.removeChild(a)
	}
	this.childs.forEach(function(c) {
		c.hide()
	})
};
JSONEditor.Node.prototype.appendChild = function(c) {
	if (this.type == "array" || this.type == "object") {
		c.setParent(this);
		c.fieldEditable = (this.type == "object");
		if (this.type == "array") {
			c.index = this.childs.length
		}
		this.childs.push(c);
		if (this.expanded) {
			var d = c.getDom();
			var a = this.getAppend();
			var b = a ? a.parentNode : undefined;
			if (a && b) {
				b.insertBefore(d, a)
			}
			c.showChilds()
		}
		this.updateDom({
			updateIndexes : true
		});
		c.updateDom({
			recurse : true
		})
	}
};
JSONEditor.Node.prototype.moveBefore = function(d, b) {
	if (this.type == "array" || this.type == "object") {
		var a = (this.dom.tr) ? this.dom.tr.parentNode : undefined;
		if (a) {
			var e = document.createElement("tr");
			e.style.height = a.clientHeight + "px";
			a.appendChild(e)
		}
		var c = d.getParent();
		if (c) {
			c.removeChild(d)
		}
		if (b instanceof JSONEditor.AppendNode) {
			this.appendChild(d)
		} else {
			this.insertBefore(d, b)
		}
		if (a) {
			a.removeChild(e)
		}
	}
};
JSONEditor.Node.prototype.moveTo = function(d, c) {
	if (d.parent == this) {
		var a = this.childs.indexOf(d);
		if (a < c) {
			c++
		}
	}
	var b = this.childs[c] || this.append;
	this.moveBefore(d, b)
};
JSONEditor.Node.prototype.insertBefore = function(f, d) {
	if (this.type == "array" || this.type == "object") {
		if (d == this.append) {
			f.setParent(this);
			f.fieldEditable = (this.type == "object");
			this.childs.push(f)
		} else {
			var c = this.childs.indexOf(d);
			if (c == -1) {
				throw new Error("Node not found")
			}
			f.setParent(this);
			f.fieldEditable = (this.type == "object");
			this.childs.splice(c, 0, f)
		}
		if (this.expanded) {
			var a = f.getDom();
			var b = d.getDom();
			var e = b ? b.parentNode : undefined;
			if (b && e) {
				e.insertBefore(a, b)
			}
			f.showChilds()
		}
		this.updateDom({
			updateIndexes : true
		});
		f.updateDom({
			recurse : true
		})
	}
};
JSONEditor.Node.prototype.search = function(h) {
	var d = [];
	var b;
	var c = h ? h.toLowerCase() : undefined;
	delete this.searchField;
	delete this.searchValue;
	if (this.field != undefined) {
		var g = String(this.field).toLowerCase();
		b = g.indexOf(c);
		if (b != -1) {
			this.searchField = true;
			d.push({
				node : this,
				elem : "field"
			})
		}
		this._updateDomField()
	}
	if (this.type == "array" || this.type == "object") {
		if (this.childs) {
			var a = [];
			this.childs.forEach(function(i) {
				a = a.concat(i.search(h))
			});
			d = d.concat(a)
		}
		if (c != undefined) {
			var e = false;
			if (a.length == 0) {
				this.collapse(e)
			} else {
				this.expand(e)
			}
		}
	} else {
		if (this.value != undefined) {
			var f = String(this.value).toLowerCase();
			b = f.indexOf(c);
			if (b != -1) {
				this.searchValue = true;
				d.push({
					node : this,
					elem : "value"
				})
			}
		}
		this._updateDomValue()
	}
	return d
};
JSONEditor.Node.prototype.scrollTo = function() {
	if (!this.dom.tr || !this.dom.tr.parentNode) {
		var b = this.parent;
		var c = false;
		while (b) {
			b.expand(c);
			b = b.parent
		}
	}
	if (this.dom.tr && this.dom.tr.parentNode) {
		var a = this.getEditor();
		if (a) {
			a.scrollTo(this.dom.tr.offsetTop)
		}
	}
};
JSONEditor.Node.prototype.focus = function(c) {
	if (this.dom.tr && this.dom.tr.parentNode) {
		if (c != "value" && this.fieldEditable) {
			var b = this.dom.field;
			if (b) {
				b.focus()
			}
		} else {
			var a = this.dom.value;
			if (a) {
				a.focus()
			}
		}
	}
};
JSONEditor.Node.prototype.blur = function() {
	this._getDomValue(false);
	this._getDomField(false)
};
JSONEditor.Node.prototype._duplicate = function(a) {
	var b = a.clone();
	this.insertBefore(b, a);
	return b
};
JSONEditor.Node.prototype.containsNode = function(c) {
	if (this == c) {
		return true
	}
	var d = this.childs;
	if (d) {
		for ( var b = 0, a = d.length; b < a; b++) {
			if (d[b].containsNode(c)) {
				return true
			}
		}
	}
	return false
};
JSONEditor.Node.prototype._move = function(b, a) {
	if (b == a) {
		return
	}
	if (b.containsNode(this)) {
		throw new Error("Cannot move a field into a child of itself")
	}
	if (b.parent) {
		b.parent.removeChild(b)
	}
	var c = b.clone();
	b.clearDom();
	if (a) {
		this.insertBefore(c, a)
	} else {
		this.appendChild(c)
	}
};
JSONEditor.Node.prototype.removeChild = function(c) {
	if (this.childs) {
		var a = this.childs.indexOf(c);
		if (a != -1) {
			c.hide();
			delete c.searchField;
			delete c.searchValue;
			var b = this.childs.splice(a, 1)[0];
			this.updateDom({
				updateIndexes : true
			});
			return b
		}
	}
	return undefined
};
JSONEditor.Node.prototype._remove = function(a) {
	this.removeChild(a)
};
JSONEditor.Node.prototype.changeType = function(b) {
	var e = this.type;
	if ((b == "string" || b == "auto") && (e == "string" || e == "auto")) {
		this.type = b
	} else {
		var d = this.dom.tr ? this.dom.tr.parentNode : undefined;
		var c;
		if (this.expanded) {
			c = this.getAppend()
		} else {
			c = this.getDom()
		}
		var a = (c && c.parentNode) ? c.nextSibling : undefined;
		this.hide();
		this.clearDom();
		this.type = b;
		if (b == "object") {
			if (!this.childs) {
				this.childs = []
			}
			this.childs.forEach(function(g, f) {
				g.clearDom();
				delete g.index;
				g.fieldEditable = true;
				if (g.field == undefined) {
					g.field = f
				}
			});
			if (e == "string" || e == "auto") {
				this.expanded = true
			}
		} else {
			if (b == "array") {
				if (!this.childs) {
					this.childs = []
				}
				this.childs.forEach(function(g, f) {
					g.clearDom();
					g.fieldEditable = false;
					g.index = f
				});
				if (e == "string" || e == "auto") {
					this.expanded = true
				}
			} else {
				this.expanded = false
			}
		}
		if (d) {
			if (a) {
				d.insertBefore(this.getDom(), a)
			} else {
				d.appendChild(this.getDom())
			}
		}
		this.showChilds()
	}
	if (b == "auto" || b == "string") {
		if (b == "string") {
			this.value = String(this.value)
		} else {
			this.value = this._stringCast(String(this.value))
		}
		this.focus()
	}
	this.updateDom({
		updateIndexes : true
	})
};
JSONEditor.Node.prototype._getDomValue = function(a) {
	if (this.dom.value && this.type != "array" && this.type != "object") {
		this.valueInnerText = JSONEditor.getInnerText(this.dom.value)
	}
	if (this.valueInnerText != undefined) {
		try {
			var d;
			if (this.type == "string") {
				d = this._unescapeHTML(this.valueInnerText)
			} else {
				var e = this._unescapeHTML(this.valueInnerText);
				d = this._stringCast(e)
			}
			if (d !== this.value) {
				var b = this.value;
				this.value = d;
				this.getEditor().onAction("editValue", {
					node : this,
					oldValue : b,
					newValue : d
				})
			}
		} catch (c) {
			this.value = undefined;
			if (a != true) {
				throw c
			}
		}
	}
};
JSONEditor.Node.prototype._updateDomValue = function() {
	var d = this.dom.value;
	if (d) {
		var b = this.value;
		var c = (this.type == "auto") ? typeof (b) : this.type;
		var a = "";
		if (c == "string") {
			a = "green"
		} else {
			if (c == "number") {
				a = "red"
			} else {
				if (c == "boolean") {
					a = "blue"
				} else {
					if (this.type == "object" || this.type == "array") {
						a = ""
					} else {
						if (b === null) {
							a = "purple"
						} else {
							if (b === undefined) {
								a = "green"
							}
						}
					}
				}
			}
		}
		d.style.color = a;
		var e = (String(this.value) == "" && this.type != "array" && this.type != "object");
		if (e) {
			JSONEditor.addClassName(d, "jsoneditor-empty")
		} else {
			JSONEditor.removeClassName(d, "jsoneditor-empty")
		}
		if (this.searchValueActive) {
			JSONEditor.addClassName(d, "jsoneditor-search-highlight-active")
		} else {
			JSONEditor.removeClassName(d, "jsoneditor-search-highlight-active")
		}
		if (this.searchValue) {
			JSONEditor.addClassName(d, "jsoneditor-search-highlight")
		} else {
			JSONEditor.removeClassName(d, "jsoneditor-search-highlight")
		}
		JSONEditor.stripFormatting(d)
	}
};
JSONEditor.Node.prototype._updateDomField = function() {
	var a = this.dom.field;
	if (a) {
		var b = (String(this.field) == "");
		if (b) {
			JSONEditor.addClassName(a, "jsoneditor-empty")
		} else {
			JSONEditor.removeClassName(a, "jsoneditor-empty")
		}
		if (this.searchFieldActive) {
			JSONEditor.addClassName(a, "jsoneditor-search-highlight-active")
		} else {
			JSONEditor.removeClassName(a, "jsoneditor-search-highlight-active")
		}
		if (this.searchField) {
			JSONEditor.addClassName(a, "jsoneditor-search-highlight")
		} else {
			JSONEditor.removeClassName(a, "jsoneditor-search-highlight")
		}
		JSONEditor.stripFormatting(a)
	}
};
JSONEditor.Node.prototype._getDomField = function(a) {
	if (this.dom.field && this.fieldEditable) {
		this.fieldInnerText = JSONEditor.getInnerText(this.dom.field)
	}
	if (this.fieldInnerText != undefined) {
		try {
			var d = this._unescapeHTML(this.fieldInnerText);
			if (d !== this.field) {
				var b = this.field;
				this.field = d;
				this.getEditor().onAction("editField", {
					node : this,
					oldValue : b,
					newValue : d
				})
			}
		} catch (c) {
			this.field = undefined;
			if (a != true) {
				throw c
			}
		}
	}
};
JSONEditor.Node.prototype.clearDom = function() {
	this.dom = {}
};
JSONEditor.Node.prototype.getDom = function() {
	var f = this.dom;
	if (f.tr) {
		return f.tr
	}
	f.tr = document.createElement("tr");
	f.tr.className = "jsoneditor-tr";
	f.tr.node = this;
	var d = document.createElement("td");
	d.className = "jsoneditor-td";
	f.drag = this._createDomDragArea();
	if (f.drag) {
		d.appendChild(f.drag)
	}
	f.tr.appendChild(d);
	var a = document.createElement("td");
	a.className = "jsoneditor-td";
	f.tr.appendChild(a);
	f.expand = this._createDomExpandButton();
	f.field = this._createDomField();
	f.value = this._createDomValue();
	f.tree = this._createDomTree(f.expand, f.field, f.value);
	a.appendChild(f.tree);
	var c = document.createElement("td");
	c.className = "jsoneditor-td jsoneditor-td-edit";
	f.tr.appendChild(c);
	f.type = this._createDomTypeButton();
	c.appendChild(f.type);
	var b = document.createElement("td");
	b.className = "jsoneditor-td jsoneditor-td-edit";
	f.tr.appendChild(b);
	f.duplicate = this._createDomDuplicateButton();
	if (f.duplicate) {
		b.appendChild(f.duplicate)
	}
	var e = document.createElement("td");
	e.className = "jsoneditor-td jsoneditor-td-edit";
	f.tr.appendChild(e);
	f.remove = this._createDomRemoveButton();
	if (f.remove) {
		e.appendChild(f.remove)
	}
	this.updateDom();
	return f.tr
};
JSONEditor.Node.prototype._onDragStart = function(b) {
	b = b || window.event;
	var a = this;
	if (!this.mousemove) {
		this.mousemove = JSONEditor.Events.addEventListener(document,
				"mousemove", function(c) {
					a._onDrag(c)
				})
	}
	if (!this.mouseup) {
		this.mouseup = JSONEditor.Events.addEventListener(document, "mouseup",
				function(c) {
					a._onDragEnd(c)
				})
	}
	JSONEditor.freezeHighlight = true;
	this.drag = {
		oldCursor : document.body.style.cursor,
		startParent : this.parent,
		startIndex : this.parent.childs.indexOf(this)
	};
	document.body.style.cursor = "move";
	JSONEditor.Events.preventDefault(b)
};
JSONEditor.Node.prototype._onDrag = function(c) {
	c = c || window.event;
	var k = this.dom.tr;
	var i = JSONEditor.getAbsoluteTop(k);
	var h = k.offsetHeight;
	var f = c.pageY || (c.clientY + document.body.scrollTop);
	if (f < i) {
		var g = k.previousSibling;
		var m = JSONEditor.getAbsoluteTop(g);
		var o = JSONEditor.getNodeFromTarget(g);
		while (g && f < m) {
			o = JSONEditor.getNodeFromTarget(g);
			g = g.previousSibling;
			m = JSONEditor.getAbsoluteTop(g)
		}
		if (o) {
			g = o.dom.tr;
			m = JSONEditor.getAbsoluteTop(g);
			if (f > m + h) {
				o = undefined
			}
		}
		if (o && o.parent) {
			o.parent.moveBefore(this, o)
		}
	} else {
		var l = (this.expanded && this.append) ? this.append.getDom()
				: this.dom.tr;
		var e = l ? l.nextSibling : undefined;
		if (e) {
			var n = JSONEditor.getAbsoluteTop(e);
			var d = undefined;
			var j = e.nextSibling;
			var b = JSONEditor.getAbsoluteTop(j);
			var a = j ? (b - n) : 0;
			while (j && f > i + a) {
				d = JSONEditor.getNodeFromTarget(j);
				j = j.nextSibling;
				b = JSONEditor.getAbsoluteTop(j);
				a = j ? (b - n) : 0
			}
			if (d && d.parent) {
				d.parent.moveBefore(this, d)
			}
		}
	}
	JSONEditor.Events.preventDefault(c)
};
JSONEditor.Node.prototype._onDragEnd = function(a) {
	a = a || window.event;
	var b = {
		node : this,
		startParent : this.drag.startParent,
		startIndex : this.drag.startIndex,
		endParent : this.parent,
		endIndex : this.parent.childs.indexOf(this)
	};
	if ((b.startParent != b.endParent) || (b.startIndex != b.endIndex)) {
		this.getEditor().onAction("moveNode", b)
	}
	document.body.style.cursor = this.drag.oldCursor;
	delete JSONEditor.freezeHighlight;
	delete this.drag;
	this.setHighlight(false);
	if (this.mousemove) {
		JSONEditor.Events.removeEventListener(document, "mousemove",
				this.mousemove);
		delete this.mousemove
	}
	if (this.mouseup) {
		JSONEditor.Events
				.removeEventListener(document, "mouseup", this.mouseup);
		delete this.mouseup
	}
	JSONEditor.Events.preventDefault(a)
};
JSONEditor.Node.prototype._createDomDragArea = function() {
	if (!this.parent) {
		return undefined
	}
	var a = document.createElement("button");
	a.className = "jsoneditor-dragarea";
	a.title = "Move field (drag and drop)";
	return a
};
JSONEditor.Node.prototype._createDomField = function() {
	return document.createElement("div")
};
JSONEditor.Node.prototype.setHighlight = function(a) {
	if (JSONEditor.freezeHighlight) {
		return
	}
	if (this.dom.tr) {
		this.dom.tr.className = "jsoneditor-tr"
				+ (a ? " jsoneditor-tr-highlight" : "");
		if (this.append) {
			this.append.setHighlight(a)
		}
		if (this.childs) {
			this.childs.forEach(function(b) {
				b.setHighlight(a)
			})
		}
	}
};
JSONEditor.Node.prototype.updateValue = function(a) {
	this.value = a;
	this.updateDom()
};
JSONEditor.Node.prototype.updateField = function(a) {
	this.field = a;
	this.updateDom()
};
JSONEditor.Node.prototype.updateDom = function(a) {
	var f = this.dom.tree;
	if (f) {
		f.style.marginLeft = this.getLevel() * 24 + "px"
	}
	var d = this.dom.field;
	if (d) {
		if (this.fieldEditable == true) {
			d.contentEditable = "true";
			d.spellcheck = false;
			d.className = "jsoneditor-field"
		} else {
			d.className = "jsoneditor-readonly"
		}
		var e;
		if (this.index != undefined) {
			e = this.index
		} else {
			if (this.field != undefined) {
				e = this.field
			} else {
				if (this.type == "array" || this.type == "object") {
					e = this.type
				} else {
					e = "field"
				}
			}
		}
		d.innerHTML = this._escapeHTML(e)
	}
	var c = this.dom.value;
	if (c) {
		var b = this.childs ? this.childs.length : 0;
		if (this.type == "array") {
			c.innerHTML = "[" + b + "]";
			c.title = this.type + " containing " + b + " items"
		} else {
			if (this.type == "object") {
				c.innerHTML = "{" + b + "}";
				c.title = this.type + " containing " + b + " items"
			} else {
				c.innerHTML = this._escapeHTML(this.value);
				delete c.title
			}
		}
	}
	this._updateDomField();
	this._updateDomValue();
	if (a && a.updateIndexes == true) {
		this._updateDomIndexes()
	}
	if (a && a.recurse == true) {
		if (this.childs) {
			this.childs.forEach(function(g) {
				g.updateDom(a)
			})
		}
		if (this.append) {
			this.append.updateDom()
		}
	}
};
JSONEditor.Node.prototype._updateDomIndexes = function() {
	var a = this.dom.value;
	var b = this.childs;
	if (a && b) {
		if (this.type == "array") {
			b.forEach(function(e, c) {
				e.index = c;
				var d = e.dom.field;
				if (d) {
					d.innerHTML = c
				}
			})
		} else {
			if (this.type == "object") {
				b.forEach(function(c) {
					if (c.index != undefined) {
						delete c.index;
						if (c.field == undefined) {
							c.field = "field"
						}
					}
				})
			}
		}
	}
};
JSONEditor.Node.prototype._createDomValue = function() {
	var a;
	if (this.type == "array") {
		a = document.createElement("div");
		a.className = "jsoneditor-readonly";
		a.innerHTML = "[...]"
	} else {
		if (this.type == "object") {
			a = document.createElement("div");
			a.className = "jsoneditor-readonly";
			a.innerHTML = "{...}"
		} else {
			if (this.type == "string") {
				a = document.createElement("div");
				a.contentEditable = "true";
				a.spellcheck = false;
				a.className = "jsoneditor-value";
				a.innerHTML = this._escapeHTML(this.value)
			} else {
				a = document.createElement("div");
				a.contentEditable = "true";
				a.spellcheck = false;
				a.className = "jsoneditor-value";
				a.innerHTML = this._escapeHTML(this.value)
			}
		}
	}
	return a
};
JSONEditor.Node.prototype._createDomExpandButton = function() {
	var b = document.createElement("button");
	var a = (this.type == "array" || this.type == "object");
	if (a) {
		b.className = this.expanded ? "jsoneditor-expanded"
				: "jsoneditor-collapsed";
		b.title = "Click to expand/collapse this field. \nCtrl+Click to expand/collapse including all childs."
	} else {
		b.className = "jsoneditor-invisible";
		b.title = ""
	}
	return b
};
JSONEditor.Node.prototype._createDomTree = function(f, d, e) {
	var g = this.dom;
	var a = document.createElement("table");
	var j = document.createElement("tbody");
	a.style.borderCollapse = "collapse";
	a.appendChild(j);
	var k = document.createElement("tr");
	j.appendChild(k);
	var h = document.createElement("td");
	h.className = "jsoneditor-td-tree";
	k.appendChild(h);
	h.appendChild(f);
	g.tdExpand = h;
	var b = document.createElement("td");
	b.className = "jsoneditor-td-tree";
	k.appendChild(b);
	b.appendChild(d);
	g.tdField = b;
	var i = document.createElement("td");
	i.className = "jsoneditor-td-tree";
	k.appendChild(i);
	if (this.type != "object" && this.type != "array") {
		i.appendChild(document.createTextNode(":"));
		i.className = "jsoneditor-separator"
	}
	g.tdSeparator = i;
	var c = document.createElement("td");
	c.className = "jsoneditor-td-tree";
	k.appendChild(c);
	c.appendChild(e);
	g.tdValue = c;
	return a
};
JSONEditor.Node.prototype.onEvent = function(b) {
	var n = b.type;
	var l = b.target || b.srcElement;
	var j = this.dom;
	var g = this;
	var c = (this.type == "array" || this.type == "object");
	var i = j.value;
	if (l == i) {
		switch (n) {
		case "focus":
			JSONEditor.focusNode = this;
			break;
		case "blur":
		case "change":
			this._getDomValue(true);
			this._updateDomValue();
			if (this.value) {
				i.innerHTML = this._escapeHTML(this.value)
			}
			break;
		case "keyup":
			this._getDomValue(true);
			this._updateDomValue();
			break;
		case "cut":
		case "paste":
			setTimeout(function() {
				g._getDomValue(true);
				g._updateDomValue()
			}, 1);
			break
		}
	}
	var f = j.field;
	if (l == f) {
		switch (n) {
		case "focus":
			JSONEditor.focusNode = this;
			break;
		case "change":
		case "blur":
			this._getDomField(true);
			this._updateDomField();
			if (this.field) {
				f.innerHTML = this._escapeHTML(this.field)
			}
			break;
		case "keyup":
			this._getDomField(true);
			this._updateDomField();
			break;
		case "cut":
		case "paste":
			setTimeout(function() {
				g._getDomField(true);
				g._updateDomField()
			}, 1);
			break
		}
	}
	var k = j.drag;
	if (l == k) {
		switch (n) {
		case "mousedown":
			this._onDragStart(b);
			break;
		case "mouseover":
			this.setHighlight(true);
			break;
		case "mouseout":
			this.setHighlight(false);
			break
		}
	}
	var h = j.expand;
	if (l == h) {
		if (n == "click") {
			if (c) {
				this._onExpand(b)
			}
		}
	}
	var p = j.duplicate;
	if (l == p) {
		switch (n) {
		case "click":
			var m = this.parent._duplicate(this);
			this.getEditor().onAction("duplicateNode", {
				node : this,
				clone : m,
				parent : this.parent
			});
			break;
		case "mouseover":
			this.setHighlight(true);
			break;
		case "mouseout":
			this.setHighlight(false);
			break
		}
	}
	var d = j.remove;
	if (l == d) {
		switch (n) {
		case "click":
			this._onRemove();
			break;
		case "mouseover":
			this.setHighlight(true);
			break;
		case "mouseout":
			this.setHighlight(false);
			break
		}
	}
	var o = j.type;
	if (l == o) {
		switch (n) {
		case "click":
			this._onChangeType(b);
			break;
		case "mouseover":
			this.setHighlight(true);
			break;
		case "mouseout":
			this.setHighlight(false);
			break
		}
	}
	var a = j.tree;
	if (l == a.parentNode) {
		switch (n) {
		case "click":
			var e = (b.offsetX != undefined) ? (b.offsetX < (this.getLevel() + 1) * 24)
					: (b.clientX < JSONEditor.getAbsoluteLeft(j.tdSeparator));
			if (e || c) {
				if (f) {
					JSONEditor.setEndOfContentEditable(f);
					f.focus()
				}
			} else {
				if (i) {
					JSONEditor.setEndOfContentEditable(i);
					i.focus()
				}
			}
			break
		}
	}
	if ((l == j.tdExpand && !c) || l == j.tdField || l == j.tdSeparator) {
		switch (n) {
		case "click":
			if (f) {
				JSONEditor.setEndOfContentEditable(f);
				f.focus()
			}
			break
		}
	}
};
JSONEditor.Node.prototype._onExpand = function(c) {
	c = c || window.event;
	var b = c.ctrlKey;
	if (b) {
		var a = this.dom.tr.parentNode;
		var e = a.parentNode;
		var d = e.scrollTop;
		e.removeChild(a)
	}
	if (this.expanded) {
		this.collapse(b)
	} else {
		this.expand(b)
	}
	if (b) {
		e.appendChild(a);
		e.scrollTop = d
	}
};
JSONEditor.Node.types = [
		{
			value : "array",
			className : "jsoneditor-option-array",
			title : 'Field type "array". An array contains an ordered collection of values.'
		},
		{
			value : "auto",
			className : "jsoneditor-option-auto",
			title : 'Field type "auto". The field type is automatically determined from the value and can be a string, number, boolean, or null.'
		},
		{
			value : "object",
			className : "jsoneditor-option-object",
			title : 'Field type "object". An object contains an unordered set of key/value pairs.'
		},
		{
			value : "string",
			className : "jsoneditor-option-string",
			title : 'Field type "string". Field type is not determined from the value, but always returned as string.'
		} ];
JSONEditor.Node.prototype._createDomTypeButton = function() {
	var b = this;
	var a = document.createElement("button");
	a.className = "jsoneditor-type-" + b.type;
	a.title = "Change field type";
	return a
};
JSONEditor.Node.prototype._onRemove = function() {
	this.setHighlight(false);
	var a = this.parent.childs.indexOf(this);
	this.parent._remove(this);
	this.getEditor().onAction("removeNode", {
		node : this,
		parent : this.parent,
		index : a
	})
};
JSONEditor.Node.prototype._onChangeType = function(d) {
	JSONEditor.Events.stopPropagation(d);
	var b = this.dom.type;
	var c = this;
	var a = JSONEditor.getAbsoluteLeft(b);
	var f = JSONEditor.getAbsoluteTop(b) + b.clientHeight;
	var e = function(g) {
		var h = c.type;
		c.changeType(g);
		c.getEditor().onAction("changeType", {
			node : c,
			oldType : h,
			newType : g
		});
		b.className = "jsoneditor-type-" + c.type
	};
	JSONEditor.showDropDownList({
		x : a,
		y : f,
		node : c,
		value : c.type,
		values : JSONEditor.Node.types,
		className : "jsoneditor-select",
		optionSelectedClassName : "jsoneditor-option-selected",
		optionClassName : "jsoneditor-option",
		callback : e
	})
};
JSONEditor.showDropDownList = function(c) {
	var b = document.createElement("div");
	b.className = c.className || "";
	b.style.position = "absolute";
	b.style.left = (c.x || 0) + "px";
	b.style.top = (c.y || 0) + "px";
	c.values.forEach(function(f) {
		var k = f.value || String(f);
		var i = "jsoneditor-option";
		var h = (k == c.value);
		if (h) {
			i += " " + c.optionSelectedClassName
		}
		var g = document.createElement("div");
		g.className = i;
		if (f.title) {
			g.title = f.title
		}
		var j = document.createElement("div");
		j.className = (f.className || "");
		g.appendChild(j);
		var e = document.createElement("div");
		e.className = "jsoneditor-option-text";
		e.innerHTML = "<div>" + k + "</div>";
		g.appendChild(e);
		g.onmousedown = (function(l) {
			return function() {
				c.callback(l)
			}
		})(f.value);
		b.appendChild(g)
	});
	document.body.appendChild(b);
	c.node.setHighlight(true);
	JSONEditor.freezeHighlight = true;
	var a = JSONEditor.Events.addEventListener(document, "mousedown",
			function() {
				JSONEditor.freezeHighlight = false;
				c.node.setHighlight(false);
				if (b && b.parentNode) {
					b.parentNode.removeChild(b)
				}
				JSONEditor.Events.removeEventListener(document, "mousedown", a)
			});
	var d = JSONEditor.Events.addEventListener(document, "mousewheel",
			function() {
				JSONEditor.freezeHighlight = false;
				c.node.setHighlight(false);
				if (b && b.parentNode) {
					b.parentNode.removeChild(b)
				}
				JSONEditor.Events
						.removeEventListener(document, "mousewheel", d)
			})
};
JSONEditor.Node.prototype.getAppend = function() {
	if (!this.append) {
		this.append = new JSONEditor.AppendNode();
		this.append.setParent(this)
	}
	return this.append.getDom()
};
JSONEditor.Node.prototype._createDomRemoveButton = function() {
	if (this.parent
			&& (this.parent.type == "array" || this.parent.type == "object")) {
		var a = document.createElement("button");
		a.className = "jsoneditor-remove";
		a.title = "Remove field (including all its childs)";
		return a
	} else {
		return undefined
	}
};
JSONEditor.Node.prototype._createDomDuplicateButton = function() {
	if (this.parent
			&& (this.parent.type == "array" || this.parent.type == "object")) {
		var a = document.createElement("button");
		a.className = "jsoneditor-duplicate";
		a.title = "Duplicate field (including all childs)";
		return a
	} else {
		return undefined
	}
};
JSONEditor.Node.prototype._getType = function(a) {
	if (a instanceof Array) {
		return "array"
	}
	if (a instanceof Object) {
		return "object"
	}
	if (typeof (a) == "string" && typeof (this._stringCast(a)) != "string") {
		return "string"
	}
	return "auto"
};
JSONEditor.Node.prototype._stringCast = function(d) {
	var c = d.toLowerCase(), b = Number(d), a = parseFloat(d);
	if (d == "") {
		return ""
	} else {
		if (c == "null") {
			return null
		} else {
			if (c == "true") {
				return true
			} else {
				if (c == "false") {
					return false
				} else {
					if (!isNaN(b) && !isNaN(a)) {
						return b
					} else {
						return d
					}
				}
			}
		}
	}
};
JSONEditor.Node.prototype._escapeHTML = function(c) {
	var a = String(c).replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(
			/  /g, " &nbsp;").replace(/^ /, "&nbsp;").replace(/ $/, "&nbsp;");
	var b = JSON.stringify(a);
	return b.substring(1, b.length - 1)
};
JSONEditor.Node.prototype._unescapeHTML = function(c) {
	var b = '"' + this._escapeJSON(c) + '"';
	var a = JSONEditor.parse(b);
	return a.replace(/&lt;/g, "<").replace(/&gt;/g, ">")
			.replace(/&nbsp;/g, " ")
};
JSONEditor.Node.prototype._escapeJSON = function(e) {
	var d = "";
	var b = 0, a = e.length;
	while (b < a) {
		var f = e.charAt(b);
		if (f == "\n") {
			d += "\\n"
		} else {
			if (f == "\\") {
				d += f;
				b++;
				f = e.charAt(b);
				if ('"\\/bfnrtu'.indexOf(f) == -1) {
					d += "\\"
				}
				d += f
			} else {
				if (f == '"') {
					d += '\\"'
				} else {
					d += f
				}
			}
		}
		b++
	}
	return d
};
JSONEditor.AppendNode = function() {
	this.dom = {}
};
JSONEditor.AppendNode.prototype = new JSONEditor.Node();
JSONEditor.AppendNode.prototype.getDom = function() {
	if (this.dom.tr) {
		return this.dom.tr
	}
	function b(e) {
		var f = document.createElement("td");
		f.className = e || "";
		return f
	}
	var a = document.createElement("tr");
	a.appendChild(b("jsoneditor-td"));
	a.node = this;
	var c = document.createElement("td");
	a.appendChild(c);
	c.className = "jsoneditor-td";
	var d = document.createElement("button");
	d.className = "jsoneditor-append";
	d.title = "Append a field";
	this.dom.append = d;
	c.appendChild(d);
	a.appendChild(b("jsoneditor-td jsoneditor-td-edit"));
	a.appendChild(b("jsoneditor-td jsoneditor-td-edit"));
	a.appendChild(b("jsoneditor-td jsoneditor-td-edit"));
	this.dom.tr = a;
	this.dom.td = c;
	this.updateDom();
	return a
};
JSONEditor.AppendNode.prototype.updateDom = function() {
	var a = this.dom.td;
	if (a) {
		a.style.paddingLeft = (this.getLevel() * 24 + 26) + "px"
	}
};
JSONEditor.AppendNode.prototype.onEvent = function(b) {
	var a = b.type;
	var d = b.target || b.srcElement;
	var e = this.dom;
	var c = e.append;
	if (d == c) {
		switch (a) {
		case "click":
			this._onAppend();
			break;
		case "mouseover":
			this.parent.setHighlight(true);
			break;
		case "mouseout":
			this.parent.setHighlight(false)
		}
	}
};
JSONEditor.AppendNode.prototype._onAppend = function() {
	var a = new JSONEditor.Node({
		field : "field",
		value : "value"
	});
	this.parent.appendChild(a);
	this.parent.setHighlight(false);
	a.focus();
	this.getEditor().onAction("appendNode", {
		node : a,
		parent : this.parent
	})
};
JSONEditor.prototype._createFrame = function() {
	this.container.innerHTML = "";
	this.frame = document.createElement("div");
	this.frame.className = "jsoneditor-frame";
	this.container.appendChild(this.frame);
	var d = this;
	var g = function(i) {
		i = i || window.event;
		var j = i.target || i.srcElement;
		var h = JSONEditor.getNodeFromTarget(j);
		if (h) {
			h.onEvent(i)
		}
	};
	this.frame.onclick = function(h) {
		g(h);
		JSONEditor.Events.preventDefault(h)
	};
	this.frame.onchange = g;
	this.frame.onkeydown = g;
	this.frame.onkeyup = g;
	this.frame.oncut = g;
	this.frame.onpaste = g;
	this.frame.onmousedown = g;
	this.frame.onmouseup = g;
	this.frame.onmouseover = g;
	this.frame.onmouseout = g;
	JSONEditor.Events.addEventListener(this.frame, "focus", g, true);
	JSONEditor.Events.addEventListener(this.frame, "blur", g, true);
	this.frame.onfocusin = g;
	this.frame.onfocusout = g;
	this.menu = document.createElement("div");
	this.menu.className = "jsoneditor-menu";
	this.frame.appendChild(this.menu);
	var e = document.createElement("button");
	e.className = "jsoneditor-menu jsoneditor-expand-all";
	e.title = "Expand all fields";
	e.onclick = function() {
		d.expandAll()
	};
	this.menu.appendChild(e);
	var a = document.createElement("button");
	a.title = "Collapse all fields";
	a.className = "jsoneditor-menu jsoneditor-collapse-all";
	a.onclick = function() {
		d.collapseAll()
	};
	this.menu.appendChild(a);
	if (this.options.history) {
		var f = document.createElement("span");
		f.innerHTML = "&nbsp;";
		this.menu.appendChild(f);
		var b = document.createElement("button");
		b.className = "jsoneditor-menu jsoneditor-undo";
		b.title = "Undo last action";
		b.onclick = function() {
			d.history.undo();
			if (d.options.change) {
				d.options.change()
			}
		};
		this.menu.appendChild(b);
		this.dom.undo = b;
		var c = document.createElement("button");
		c.className = "jsoneditor-menu jsoneditor-redo";
		c.title = "Redo";
		c.onclick = function() {
			d.history.redo();
			if (d.options.change) {
				d.options.change()
			}
		};
		this.menu.appendChild(c);
		this.dom.redo = c;
		this.history.onChange = function() {
			b.disabled = !d.history.canUndo();
			c.disabled = !d.history.canRedo()
		};
		this.history.onChange()
	}
	if (this.options.search) {
		this.searchBox = new JSONEditor.SearchBox(this, this.menu)
	}
};
JSONEditor.prototype._createTable = function() {
	var a = document.createElement("div");
	a.className = "jsoneditor-content-outer";
	this.contentOuter = a;
	this.content = document.createElement("div");
	this.content.className = "jsoneditor-content";
	a.appendChild(this.content);
	this.table = document.createElement("table");
	this.table.className = "jsoneditor-table";
	this.content.appendChild(this.table);
	var c = JSONEditor.getInternetExplorerVersion();
	if (c == 8) {
		this.content.style.overflow = "scroll"
	}
	var b;
	this.colgroupContent = document.createElement("colgroup");
	b = document.createElement("col");
	b.width = "24px";
	this.colgroupContent.appendChild(b);
	b = document.createElement("col");
	this.colgroupContent.appendChild(b);
	b = document.createElement("col");
	b.width = "24px";
	this.colgroupContent.appendChild(b);
	b = document.createElement("col");
	b.width = "24px";
	this.colgroupContent.appendChild(b);
	b = document.createElement("col");
	b.width = "24px";
	this.colgroupContent.appendChild(b);
	this.table.appendChild(this.colgroupContent);
	this.tbody = document.createElement("tbody");
	this.table.appendChild(this.tbody);
	this.frame.appendChild(a)
};
JSONEditor.getNodeFromTarget = function(a) {
	while (a) {
		if (a.node) {
			return a.node
		}
		a = a.parentNode
	}
	return undefined
};
JSONFormatter = function(b, d, e) {
	if (!JSON) {
		throw new Error(
				"Your browser does not support JSON. \n\nPlease install the newest version of your browser.\n(all modern browsers support JSON).")
	}
	this.container = b;
	this.indentation = 4;
	this.width = b.clientWidth;
	this.height = b.clientHeight;
	this.frame = document.createElement("div");
	this.frame.className = "jsoneditor-frame";
	this.frame.onclick = function(h) {
		JSONEditor.Events.preventDefault(h)
	};
	this.menu = document.createElement("div");
	this.menu.className = "jsoneditor-menu";
	this.frame.appendChild(this.menu);
	var g = document.createElement("button");
	g.className = "jsoneditor-menu jsoneditor-format";
	g.title = "Format JSON data, with proper indentation and line feeds";
	this.menu.appendChild(g);
	var c = document.createElement("button");
	c.className = "jsoneditor-menu jsoneditor-compact";
	c.title = "Compact JSON data, remove all whitespaces";
	this.menu.appendChild(c);
	this.content = document.createElement("div");
	this.content.className = "jsonformatter-content";
	this.frame.appendChild(this.content);
	this.textarea = document.createElement("textarea");
	this.textarea.className = "jsonformatter-textarea";
	this.textarea.spellcheck = false;
	this.content.appendChild(this.textarea);
	var a = this.textarea;
	if (d) {
		if (d.change) {
			if (this.textarea.oninput === null) {
				this.textarea.oninput = function() {
					d.change()
				}
			} else {
				this.textarea.onchange = function() {
					d.change()
				}
			}
		}
		if (d.indentation) {
			this.indentation = Number(d.indentation)
		}
	}
	var f = this;
	g.onclick = function() {
		try {
			var h = JSONEditor.parse(a.value);
			a.value = JSON.stringify(h, null, f.indentation)
		} catch (i) {
			f.onError(i)
		}
	};
	c.onclick = function() {
		try {
			var h = JSONEditor.parse(a.value);
			a.value = JSON.stringify(h)
		} catch (i) {
			f.onError(i)
		}
	};
	this.container.appendChild(this.frame);
	if (typeof (e) == "string") {
		this.setText(e)
	} else {
		this.set(e)
	}
};
JSONFormatter.prototype.onError = function(a) {
};
JSONFormatter.prototype.set = function(a) {
	this.textarea.value = JSON.stringify(a, null, this.indentation)
};
JSONFormatter.prototype.get = function() {
	return JSONEditor.parse(this.textarea.value)
};
JSONFormatter.prototype.getText = function() {
	return this.textarea.value
};
JSONFormatter.prototype.setText = function(a) {
	this.textarea.value = a
};
JSONEditor.SearchBox = function(g, a) {
	var m = this;
	this.editor = g;
	this.timeout = undefined;
	this.delay = 200;
	this.lastText = undefined;
	this.dom = {};
	this.dom.container = a;
	var n = document.createElement("table");
	this.dom.table = n;
	n.className = "jsoneditor-search";
	a.appendChild(n);
	var e = document.createElement("tbody");
	this.dom.tbody = e;
	n.appendChild(e);
	var j = document.createElement("tr");
	e.appendChild(j);
	var c = document.createElement("td");
	c.className = "jsoneditor-search";
	j.appendChild(c);
	var d = document.createElement("div");
	this.dom.results = d;
	d.className = "jsoneditor-search-results";
	c.appendChild(d);
	c = document.createElement("td");
	c.className = "jsoneditor-search";
	j.appendChild(c);
	var l = document.createElement("div");
	this.dom.input = l;
	l.className = "jsoneditor-search";
	l.title = "Search fields and values";
	c.appendChild(l);
	var b = document.createElement("table");
	b.className = "jsoneditor-search-input";
	l.appendChild(b);
	var i = document.createElement("tbody");
	b.appendChild(i);
	j = document.createElement("tr");
	i.appendChild(j);
	var k = document.createElement("button");
	k.className = "jsoneditor-search-refresh";
	c = document.createElement("td");
	c.appendChild(k);
	j.appendChild(c);
	var o = document.createElement("input");
	this.dom.search = o;
	o.className = "jsoneditor-search";
	o.oninput = function(p) {
		m.onDelayedSearch(p)
	};
	o.onchange = function(p) {
		m.onSearch(p)
	};
	o.onkeydown = function(p) {
		m.onKeyDown(p)
	};
	o.onkeyup = function(p) {
		m.onKeyUp(p)
	};
	k.onclick = function(p) {
		o.select()
	};
	c = document.createElement("td");
	c.appendChild(o);
	j.appendChild(c);
	var f = document.createElement("button");
	f.title = "Next result (Enter)";
	f.className = "jsoneditor-search-next";
	f.onclick = function() {
		m.next()
	};
	c = document.createElement("td");
	c.appendChild(f);
	j.appendChild(c);
	var h = document.createElement("button");
	h.title = "Previous result (Shift+Enter)";
	h.className = "jsoneditor-search-previous";
	h.onclick = function() {
		m.previous()
	};
	c = document.createElement("td");
	c.appendChild(h);
	j.appendChild(c)
};
JSONEditor.SearchBox.prototype.next = function() {
	if (this.results != undefined) {
		var a = (this.resultIndex != undefined) ? this.resultIndex + 1 : 0;
		if (a > this.results.length - 1) {
			a = 0
		}
		this.setActiveResult(a)
	}
};
JSONEditor.SearchBox.prototype.previous = function() {
	if (this.results != undefined) {
		var a = this.results.length - 1;
		var b = (this.resultIndex != undefined) ? this.resultIndex - 1 : a;
		if (b < 0) {
			b = a
		}
		this.setActiveResult(b)
	}
};
JSONEditor.SearchBox.prototype.setActiveResult = function(c) {
	if (this.activeResult) {
		var b = this.activeResult.node;
		var a = this.activeResult.elem;
		if (a == "field") {
			delete b.searchFieldActive
		} else {
			delete b.searchValueActive
		}
		b.updateDom()
	}
	if (!this.results || !this.results[c]) {
		this.resultIndex = undefined;
		this.activeResult = undefined;
		return
	}
	this.resultIndex = c;
	var e = this.results[this.resultIndex].node;
	var d = this.results[this.resultIndex].elem;
	if (d == "field") {
		e.searchFieldActive = true
	} else {
		e.searchValueActive = true
	}
	this.activeResult = this.results[this.resultIndex];
	e.updateDom();
	e.scrollTo()
};
JSONEditor.SearchBox.prototype.focusActiveResult = function() {
	if (!this.activeResult) {
		this.next()
	}
	if (this.activeResult) {
		this.activeResult.node.focus(this.activeResult.elem)
	}
};
JSONEditor.SearchBox.prototype.clearDelay = function() {
	if (this.timeout != undefined) {
		clearTimeout(this.timeout);
		delete this.timeout
	}
};
JSONEditor.SearchBox.prototype.onDelayedSearch = function(a) {
	this.clearDelay();
	var b = this;
	this.timeout = setTimeout(function(c) {
		b.onSearch(c)
	}, this.delay)
};
JSONEditor.SearchBox.prototype.onSearch = function(b, d) {
	this.clearDelay();
	var c = this.dom.search.value;
	var e = (c.length > 0) ? c : undefined;
	if (e != this.lastText || d) {
		this.lastText = e;
		this.results = editor.search(e);
		this.setActiveResult(undefined);
		if (e != undefined) {
			var a = this.results.length;
			switch (a) {
			case 0:
				this.dom.results.innerHTML = "no&nbsp;results";
				break;
			case 1:
				this.dom.results.innerHTML = "1&nbsp;result";
				break;
			default:
				this.dom.results.innerHTML = a + "&nbsp;results";
				break
			}
		} else {
			this.dom.results.innerHTML = ""
		}
	}
};
JSONEditor.SearchBox.prototype.onKeyDown = function(a) {
	a = a || window.event;
	var b = a.which || a.keyCode;
	if (b == 27) {
		this.dom.search.value = "";
		this.onSearch(a);
		JSONEditor.Events.preventDefault(a);
		JSONEditor.Events.stopPropagation(a)
	} else {
		if (b == 13) {
			if (a.ctrlKey) {
				this.onSearch(a, true)
			} else {
				if (a.shiftKey) {
					this.previous()
				} else {
					this.next()
				}
			}
			JSONEditor.Events.preventDefault(a);
			JSONEditor.Events.stopPropagation(a)
		}
	}
};
JSONEditor.SearchBox.prototype.onKeyUp = function(a) {
	a = a || window.event;
	var b = a.which || a.keyCode;
	if (b != 27 && b != 13) {
		this.onDelayedSearch(a)
	}
};
JSONEditor.Events = {};
JSONEditor.Events.addEventListener = function(b, e, d, a) {
	if (b.addEventListener) {
		if (a === undefined) {
			a = false
		}
		if (e === "mousewheel" && navigator.userAgent.indexOf("Firefox") >= 0) {
			e = "DOMMouseScroll"
		}
		b.addEventListener(e, d, a);
		return d
	} else {
		var c = function() {
			return d.call(b, window.event)
		};
		b.attachEvent("on" + e, c);
		return c
	}
};
JSONEditor.Events.removeEventListener = function(b, d, c, a) {
	if (b.removeEventListener) {
		if (a === undefined) {
			a = false
		}
		if (d === "mousewheel" && navigator.userAgent.indexOf("Firefox") >= 0) {
			d = "DOMMouseScroll"
		}
		b.removeEventListener(d, c, a)
	} else {
		b.detachEvent("on" + d, c)
	}
};
JSONEditor.Events.stopPropagation = function(a) {
	if (!a) {
		a = window.event
	}
	if (a.stopPropagation) {
		a.stopPropagation()
	} else {
		a.cancelBubble = true
	}
};
JSONEditor.Events.preventDefault = function(a) {
	if (!a) {
		a = window.event
	}
	if (a.preventDefault) {
		a.preventDefault()
	} else {
		a.returnValue = false
	}
};
JSONEditor.getAbsoluteLeft = function(b) {
	var c = 0;
	var a = document.body;
	while (b != null && b != a) {
		c += b.offsetLeft;
		c -= b.scrollLeft;
		b = b.offsetParent
	}
	return c
};
JSONEditor.getAbsoluteTop = function(b) {
	var c = 0;
	var a = document.body;
	while (b != null && b != a) {
		c += b.offsetTop;
		c -= b.scrollTop;
		b = b.offsetParent
	}
	return c
};
JSONEditor.addClassName = function(c, b) {
	var a = c.className.split(" ");
	if (a.indexOf(b) == -1) {
		a.push(b);
		c.className = a.join(" ")
	}
};
JSONEditor.removeClassName = function(d, c) {
	var b = d.className.split(" ");
	var a = b.indexOf(c);
	if (a != -1) {
		b.splice(a, 1);
		d.className = b.join(" ")
	}
};
JSONEditor.stripFormatting = function(b) {
	var g = b.childNodes;
	for ( var e = 0, a = g.length; e < a; e++) {
		var h = g[e];
		if (h.style) {
			h.removeAttribute("style")
		}
		var c = h.attributes;
		if (c) {
			for ( var d = c.length - 1; d >= 0; d--) {
				var f = c[d];
				if (f.specified == true) {
					h.removeAttribute(f.name)
				}
			}
		}
		JSONEditor.stripFormatting(h)
	}
};
JSONEditor.setEndOfContentEditable = function(b) {
	var a, c;
	if (document.createRange) {
		a = document.createRange();
		a.selectNodeContents(b);
		a.collapse(false);
		c = window.getSelection();
		c.removeAllRanges();
		c.addRange(a)
	} else {
		if (document.selection) {
			a = document.body.createTextRange();
			a.moveToElementText(b);
			a.collapse(false);
			a.select()
		}
	}
};
JSONEditor.getInnerText = function(d, b) {
	var f = (b == undefined);
	if (f) {
		b = {
			text : "",
			flush : function() {
				var i = this.text;
				this.text = "";
				return i
			},
			set : function(i) {
				this.text = i
			}
		}
	}
	if (d.nodeValue) {
		return b.flush() + d.nodeValue
	}
	if (d.hasChildNodes()) {
		var k = d.childNodes;
		var g = "";
		for ( var e = 0, c = k.length; e < c; e++) {
			var a = k[e];
			if (a.nodeName == "DIV" || a.nodeName == "P") {
				var j = k[e - 1];
				var h = j ? j.nodeName : undefined;
				if (h && h != "DIV" && h != "P" && h != "BR") {
					g += "\n";
					b.flush()
				}
				g += JSONEditor.getInnerText(a, b);
				b.set("\n")
			} else {
				if (a.nodeName == "BR") {
					g += b.flush();
					b.set("\n")
				} else {
					g += JSONEditor.getInnerText(a, b)
				}
			}
		}
		return g
	} else {
		if (d.nodeName == "P" && JSONEditor.getInternetExplorerVersion() != -1) {
			return b.flush()
		}
	}
	return ""
};
JSONEditor._ieVersion = undefined;
JSONEditor.getInternetExplorerVersion = function() {
	if (JSONEditor._ieVersion == undefined) {
		var c = -1;
		if (navigator.appName == "Microsoft Internet Explorer") {
			var a = navigator.userAgent;
			var b = new RegExp("MSIE ([0-9]{1,}[.0-9]{0,})");
			if (b.exec(a) != null) {
				c = parseFloat(RegExp.$1)
			}
		}
		JSONEditor._ieVersion = c
	}
	return JSONEditor._ieVersion
};
JSONEditor.ieVersion = JSONEditor.getInternetExplorerVersion();
JSONEditor.parse = function(a) {
	try {
		return JSON.parse(a)
	} catch (c) {
		var b = JSONEditor.validate(a) || c;
		throw new Error(b)
	}
};
JSONEditor.validate = function(a) {
	var c = undefined;
	try {
		if (window.jsonlint) {
			window.jsonlint.parse(a)
		} else {
			JSON.parse(a)
		}
	} catch (b) {
		c = '<pre class="error">' + b.toString() + "</pre>";
		if (window.jsonlint) {
			c += '<a class="error" href="http://zaach.github.com/jsonlint/" target="_blank">validated by jsonlint</a>'
		}
	}
	return c
};