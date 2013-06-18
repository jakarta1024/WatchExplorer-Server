package application.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import application.dao.StoreAdministratorDao;

/**
 * Servlet implementation class RemoteInputChecker
 */
@WebServlet("/remotecheck")
public class FieldsCheckController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FieldsCheckController() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String randomInput = request.getParameter("validator");
		String validatorId = request.getParameter("id");
		if (randomInput != null && !randomInput.isEmpty()) {
			boolean passed = false;
			String validaterString = request.getSession().getAttribute(validatorId).toString();
			if (validaterString.equalsIgnoreCase(randomInput)) {
				passed = true;
			}
			response.getOutputStream().print(passed);
			return;
		}
		String userEmailInput = request.getParameter("email");
		if (userEmailInput != null && !userEmailInput.isEmpty()){
			boolean existing = false;
			StoreAdministratorDao sadminDAO = new StoreAdministratorDao();
			if (sadminDAO.getStoreAdminByEmail(userEmailInput) != null) {
				existing = true;
			}
			response.getOutputStream().print(!existing);
			return;
		}
		
		String approvedFounder = request.getParameter("founder");
		if (approvedFounder != null && !approvedFounder.isEmpty()) {
			boolean existing = false;
			StoreAdministratorDao sadminDAO = new StoreAdministratorDao();
			if (sadminDAO.getStoreAdminByName(approvedFounder)!=null) {
				existing = true;
			}
			response.getOutputStream().print(!existing);
		}
		
	}

}
