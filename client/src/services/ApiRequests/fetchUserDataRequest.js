import axiosInstance from "../axiosInstance";

const fetchUserDataRequest = async () => {
    const response = await axiosInstance.get("/feed/getUserData");
    return response;
}

export default fetchUserDataRequest;