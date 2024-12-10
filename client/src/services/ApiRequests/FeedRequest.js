import axiosInstanceWithNoErrorChecks from "../axiosInstanceWithNoErrorChecks";

const getContentFeedRequest = async (feedType, param, page, limit) => {

    if (feedType === "home") {
        try {
            const response = await axiosInstanceWithNoErrorChecks.get(`/feed/${feedType}/${limit}`, {});
            return response;
        } catch (err) {
            if (err.response.status === 403) {
                window.alert("Your session has expired. Please log in again.");
                window.location.href = "/auth";
            }
            return err.response;
        }
    } else if (feedType === "user") {
        try {
            const response = await axiosInstanceWithNoErrorChecks.get(`/feed/${feedType}/${param}/${page}/${limit}`, {});
            return response;
        } catch (err) {
            if (err.response.status === 403) {
                window.alert("Your session has expired. Please log in again.");
                window.location.href = "/auth";
            }
            return err.response;
        }

    } else if (feedType === "tag") {
        try {
            const response = await axiosInstanceWithNoErrorChecks.get(`/feed/${feedType}/${param}/${page}/${limit}`, {});
            return response;
        } catch (err) {
            if (err.response.status === 403) {
                window.alert("Your session has expired. Please log in again.");
                window.location.href = "/auth";
            }
            return err.response;
        }
    } else if (feedType = "justBasicUserDataWhenSearched") {
        try {
            const response = await axiosInstanceWithNoErrorChecks.get(`/feed/userDataWhenSearched/${param}`, {}, { withCredentials: true });
            return response;
        } catch (err) {
            if (err.response.status === 403) {
                window.alert("Your session has expired. Please log in again.");
                window.location.href = "/auth";
            }
            return err.response;
        }
    }
}

export default getContentFeedRequest;