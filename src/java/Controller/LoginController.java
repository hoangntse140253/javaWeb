/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dao.RegistrationDAO;
import dto.RegistrationErrorObject;

/**
 *
 * @author Admin
 */
@WebServlet(name = "LoginController", urlPatterns = {"/LoginController"})
public class LoginController extends HttpServlet {
    private static final String ERROR = "error.jsp";
    private static final String ADMIN = "admin.jsp";
    private static final String USER = "test.jsp";
    private static final String INVALID = "index.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = ERROR;
        try {
            String username = request.getParameter("txtUsername");
            String password = request.getParameter("txtPassword");
            boolean valid = true;
            RegistrationErrorObject errorObj = new RegistrationErrorObject();
            if(username.length() == 0) {
                valid = false;
                errorObj.setUsernameError("Username can't be blank");
            }
            if(password.length() == 0) {
                valid = false;
                errorObj.setPasswordError("Password can't be blank");
            }
            if(valid) {
                RegistrationDAO dao = new RegistrationDAO();
                String role = dao.checkLogin(username, password);
                if(role.equals("failed")) {
                    request.setAttribute("ERROR", "Invalid Username or Password");
                } else {
                    HttpSession session = request.getSession();
                    session.setAttribute("USER", username);
                    session.setAttribute("ROLE", role);
                    if(role.equals("admin")) {
                        url = ADMIN;
                    } else if(role.equals("user")) {
                        url = USER;
                    } else request.setAttribute("ERROR", "Your role is invalid");
                }
            } else {
                request.setAttribute("INVALID", errorObj);
                url = INVALID;
            }
        } catch (Exception e) {
            log("ERROR at LoginController: " + e.getMessage());
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
