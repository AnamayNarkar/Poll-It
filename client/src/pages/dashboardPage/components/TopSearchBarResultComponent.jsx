import React from 'react'
import '../styles/TopSearchBarResultComponentStyles.css'
import { useNavigate } from 'react-router-dom';

const TopSearchBarResultComponent = ({ result, typeOfSearch }) => {

    const navigate = useNavigate();

    return (
        <div className='singularSearchResultContainer' onClick={() => {
            if (typeOfSearch === 'tag') {
                navigate(`/t/${result.name}`);
            } else if (typeOfSearch === 'user') {
                navigate(`/u/${result.username}`);
            }
        }}>
            <div className='searchResultName' >
                {typeOfSearch === 'tag' ? "t/" + result.name : typeOfSearch === 'user' ? "u/" + result.username : result.name}
            </div>
        </div >
    )
}

export default TopSearchBarResultComponent
