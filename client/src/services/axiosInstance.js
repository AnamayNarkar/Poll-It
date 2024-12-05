import axios from 'axios';


const axiosInstance = axios.create({
    baseURL: 'http://localhost:3000/api',
    withCredentials: true,
});

axiosInstance.interceptors.response.use(

    (response) => {
        return response;
    },
    (error) => {
        if (error.response && error.response.status === 403) {
            window.alert("Your session has expired. Please log in again.");
            window.location.href = "/auth";
        } else {
            window.alert(error.response?.data?.message || "An error occurred");
        }
        return Promise.reject(error);
    }

);

export default axiosInstance;