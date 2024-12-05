import axiosInstance from "../axiosInstance";

const searchForTagsRequest = async (searchTerm) => {
    const response = await axiosInstance.get(`/search/getTagsLike/${searchTerm}`);
    return response.data;
}

export default searchForTagsRequest;