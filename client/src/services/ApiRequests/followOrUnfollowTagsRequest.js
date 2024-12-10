import axiosInstanceWithNoErrorChecks from "../axiosInstanceWithNoErrorChecks";

const followOrUnfollowTagsRequest = async (tagName) => {
    try {
        const response = await axiosInstanceWithNoErrorChecks.post(`/tag/followOrUnfollowTag/${tagName}`, {});
        return response;
    } catch (err) {
        return err.response;
    }
}

export default followOrUnfollowTagsRequest;