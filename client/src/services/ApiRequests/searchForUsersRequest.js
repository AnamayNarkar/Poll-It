import axiosInstance from "../axiosInstance";

const searchForUsersRequest = async (searchTerm) => {
    const response = await axiosInstance.get(`/search/getUsersLike/${searchTerm}`);
    return response.data;
}

export default searchForUsersRequest;