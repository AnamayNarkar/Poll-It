import React from 'react'
import '../styles/DashboardHeaderStyles.css'

const DashboardHeader = () => {
    return (
        <div className='dashboardHeaderContainer'>
            <div className="logoAndName">
                <img src="https://github.com/user-attachments/assets/fb215190-eb84-4f61-aa51-97ca581d6196" alt="logo" className='dashboardHeaderLogo' />
                <h2>PollIt</h2>
            </div>
            <div className='searchBar'>
                <input type='text' placeholder='Search' className='searchInput' />
            </div>
            <div className='profileIcon'>
                <img src="https://github.com/user-attachments/assets/66ffe0b4-47fc-4344-a575-2627f4f9be6a" alt="profile" className='profileImage' />
            </div>
        </div>
    )
}

export default DashboardHeader