import axios from "axios";
import { useNavigate } from "react-router-dom";

const loginRequest = async (loginFormData) => {
    try {
        const response = await axios.post("http://localhost:3000/api/auth/login", loginFormData, { withCredentials: true });
        if (response.status < 200 || response.status >= 300) {
            throw new Error(response.data.message);
        }
        return response;
    } catch (error) {
        window.alert(error.response?.data?.message || "An error occurred");
        throw error.response.data;
    }

}

const registrationRequest = async (registrationFormData) => {
    try {
        const response = await axios.post("http://localhost:3000/api/auth/register", registrationFormData, { withCredentials: true });
        if (response.status < 200 || response.status >= 300) {
            throw new Error(response.data.message);
        }
        return response;
    } catch (error) {
        window.alert(error.response?.data?.message || "An error occurred");
        throw error.response.data;
    }
}

const logoutRequest = async () => {
    try {
        const response = await axios.post("http://localhost:3000/api/auth/logout", {}, { withCredentials: true });
        window.location.reload();
    } catch (error) {
        window.alert("Error logging out");
        return error.response;
    }
}

export { loginRequest, registrationRequest, logoutRequest };