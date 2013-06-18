(function($){
	$.extend($.fn, {
		preview: function ( options ) {
			var defaults = {
					type: 'image',
					defaults: [],
					upload: true,
					seperator: ',',
					maxPreviewWidth: 600,
					maxPreviewHeight: 300
				},
				EXTENSION = {
					image: ['jpg', 'png', 'jpeg'],
					text: ['txt']
				},
				opts = $.extend( {}, defaults, options ),
			    $this = $(this),
			    $uploader_c = $this,
			    $thumb_c, $thumb_u, $thumb_a, $thumb_p, $thumb_d,
			    THUMB_CONTAINER = '<div class="image_preview"></div>',
			    THUMB_UNIT = '<div class="thumb_unit">'+
			                       '<img width="100px" height="100px" class="thumb_view" />'+
			                       '<img width="16px" height="16px" class="thumb_delete" src="./resources/img/del_pic.png" />'+
                                   '<input name="imagesel" type="file" class="thumb_input_button" /></div>',
                $preview_u, $preview_v,
                PREVIEW_UNIT = '<div class="preview_unit"><img class="preview_view" /></div>',
                $delete_t,
                DELETE_TAG = '<input type="text" name="deletedImage" class="delete_tag"/>',
                DEFAULT_IMAGE_URL = "./resources/img/add_pic.png";
			init();
			
			function init () {
				opts.exts = EXTENSION[opts.type];
				$uploader_c.addClass('image_preview');
				$thumb_c = $(THUMB_CONTAINER);
				$preview_u = $(PREVIEW_UNIT);
				$preview_v = $preview_u.find('img');
				$delete_t = $(DELETE_TAG);
				$uploader_c.append($thumb_c, $preview_u, $delete_t);
				for (var i = 0; i <= opts.defaults.length; i++) {
					add(opts.defaults[i]);
				}
			}
			
			function add (url) {
				if (!url && !opts.upload) {
					return;
				}
				$thumb_u = $(THUMB_UNIT);
				$thumb_c.append($thumb_u);
				$thumb_a = $thumb_u.find('input[type="file"]');
				$thumb_p = $thumb_u.find('img.thumb_view');
				$thumb_d = $thumb_u.find('img.thumb_delete');
				if (!!url) {
					$thumb_p.attr('src', url);
					$thumb_a.hide();
					if (opts.upload) {
						$thumb_d.show();
					}
				} else if (opts.upload) {
					$thumb_p.attr('src', DEFAULT_IMAGE_URL);
					$thumb_a.bind('change', function(){
						var path = this.value,
						    file = this.files[0],
						    ext = path.substr(path.lastIndexOf('.') + 1);
						if (opts.exts.indexOf(ext) === -1) {
				            return;
				        }
				        if (WS.Browser.ff()) {
				            path = window.URL.createObjectURL(file);
				        }
				        if (!!file && window.File && window.FileReader && window.FileList && window.Blob) {
				            var fileReader = new FileReader();
				            fileReader.onload = function (evt) {
				            	$thumb_p.attr('src', evt.target.result);
				            	$thumb_a.hide();
				            	$thumb_d.show();
				            	add();
				            };
				            fileReader.onerror = function (evt) {
				                console.error('error', evt);
				            };
				            fileReader.readAsDataURL(file);
						}
					});
				}
				$thumb_p.bind('click', function(){
					$preview_u.show();
					$preview_v.attr('src', $(this).attr('src'));
					$preview_v.removeAttr('width');
					$preview_v.removeAttr('height');
					var width = $preview_v[0].offsetWidth,
					    height = $preview_v[0].offsetHeight,
					    HW = height / width,
					    WH = width / height;
				    if (width > opts.maxPreviewWidth) {
				    	$preview_v.attr('width', opts.maxPreviewWidth);
				    	$preview_v.attr('height', opts.maxPreviewWidth * HW);
				    }
				    if (height > opts.maxPreviewHeight) {
				    	$preview_v.attr('width', opts.maxPreviewHeight * WH);
				    	$preview_v.attr('height', opts.maxPreviewHeight);
				    }
			    });
				$thumb_d.bind('click', function(){
					$(this).parent().remove();
				});
			}
		
			return {};
		}
	});
})(jQuery);