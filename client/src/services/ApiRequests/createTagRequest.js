import axiosInstanceWithNoErrorChecks from "../axiosInstanceWithNoErrorChecks";

const createTagRequest = async (tagName) => {

    try {
        const response = await axiosInstanceWithNoErrorChecks.post(`/tag/createTag/${tagName}`, {});
        return response;
    } catch (error) {
        if (error.response.status === 403) {
            window.alert("Your session has expired. Please log in again.");
            window.location.href = "/auth";
        } else {
            return error.response;
        }
    }

}

export default createTagRequest;