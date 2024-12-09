import React, { useEffect, useState } from 'react'
import DashboardHeader from './components/DashboardHeader'
import MainContent from './components/MainContent'
import { useParams } from 'react-router-dom'
import fetchUserDataRequest from '../../services/ApiRequests/fetchUserDataRequest'

const DashboardPage = ({ feedType }) => {

    const { usernameParam, tagParam } = useParams();

    const [userData, setUserData] = React.useState({});

    const fetchUserData = async () => {
        const userData = await fetchUserDataRequest();
        setUserData(userData.data);
    }

    useEffect(() => {
        fetchUserData();
    }, []);

    return (
        <div className='dashboardPageContainer' style={{ backgroundColor: '#242424', height: '100%', width: '100%', margin: '0', padding: '0', display: 'flex', flexDirection: 'column' }}>
            <DashboardHeader />
            <MainContent followedTags={userData.followedTags ? userData.followedTags : []} feedType={feedType} param={usernameParam != null ? usernameParam : tagParam != null ? tagParam : null} />
        </div >
    )
}

export default DashboardPage