import axios from "axios";

const loginRequest = async (loginFormData) => {
    try {
        const response = await axios.post("http://localhost:3000/api/user/login", loginFormData, { withCredentials: true });
        if (response.status < 200 || response.status >= 300) {
            throw new Error(response.data.message);
        }
        return response;
    } catch (error) {
        window.alert(error.response?.data?.message || "An error occurred");
        throw error.response.data;
    }

}

export default loginRequest;