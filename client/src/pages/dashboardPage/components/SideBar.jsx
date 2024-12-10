import React from 'react'
import '../styles/SideBarStyles.css'
import { useNavigate } from 'react-router-dom'

const SideBar = ({ followedTags }) => {

    const navigate = useNavigate();

    return (
        <div className='sideBarContainer'>
            <div className='homeAndPopular'>
                <div className='homeOption' onClick={() => {
                    navigate(`/home`);
                }}>
                    <h4>Home</h4>
                </div>
                <div className='popularOption'>
                    <h4>Popular</h4>
                </div>
            </div>
            <div className='tagsYouFollow'>
                <div className='tagsYouFollowHeader'>
                    <h4>Tags you follow</h4>
                </div>
                <div className='tagsYouFollowList'>
                    {
                        followedTags?.map(tag => {
                            return (
                                <div key={tag.id} className='tagYouFollowIndividual' onClick={() => {
                                    navigate(`/t/${tag.name}`);
                                    window.location.reload();
                                }}>
                                    <h5>t/{tag.name}</h5>
                                </div>
                            )
                        })
                    }
                </div>
            </div>
        </div >
    )
}

export default SideBar