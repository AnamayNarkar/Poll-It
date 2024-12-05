import axios from 'axios';

const axiosInstanceWithNoErrorChecks = axios.create({
    baseURL: 'http://localhost:3000/api',
    withCredentials: true,
});


export default axiosInstanceWithNoErrorChecks;