import React, { useEffect, useState } from 'react'
import DashboardHeader from './components/DashboardHeader'
import MainContent from './components/MainContent'
import { useParams } from 'react-router-dom'
import fetchUserDataRequest from '../../services/ApiRequests/fetchUserDataRequest'

const DashboardPage = ({ feedType }) => {

    const { usernameParam, tagParam, pollParam } = useParams();
    const [userData, setUserData] = React.useState({});
    const [followedTags, setFollowedTags] = useState([]);

    const fetchUserData = async () => {
        const response = await fetchUserDataRequest();
        setUserData(response.data.data);
        setFollowedTags([...response.data.data.followedTags]);
    }

    useEffect(() => {
        fetchUserData();
    }, []);

    return (
        <div className='dashboardPageContainer' style={{ backgroundColor: '#242424', height: '100%', width: '100%', margin: '0', padding: '0', display: 'flex', flexDirection: 'column' }}>
            <DashboardHeader feedType={feedType} param={usernameParam != null ? usernameParam : tagParam != null ? tagParam : pollParam != null ? pollParam : null} userData={userData} />
            <MainContent followedTags={followedTags} feedType={feedType} param={usernameParam != null ? usernameParam : tagParam != null ? tagParam : pollParam != null ? pollParam : null} setFollowedTags={setFollowedTags} userData={userData} />
        </div >
    )
}

export default DashboardPage