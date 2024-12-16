import React, { useEffect, useRef, useState } from 'react';
import '../styles/DashboardHeaderStyles.css';
import TopSearchBarResultComponent from './TopSearchBarResultComponent';
import searchForTagsRequest from '../../../services/ApiRequests/searchForTagsRequest';
import searchForUsersRequest from '../../../services/ApiRequests/searchForUsersRequest';
import { useNavigate } from 'react-router-dom';

const DashboardHeader = ({ feedType, param, userData }) => {

    const navigate = useNavigate();

    const [areSearchResultsVisible, setAreSearchResultsVisible] = useState(false);
    const [searchResults, setSearchResults] = useState([]);
    const [searchString, setSearchString] = useState('');
    const [debouncedSearchString, setDebouncedSearchString] = useState('');
    const [userHasTyped, setUserHasTyped] = useState(false);
    const searchInputRef = useRef(null);
    const searchResultsContainerRef = useRef(null);

    const fetchTags = async (query) => {
        try {
            const response = await searchForTagsRequest(query);
            setSearchResults(response.data);
        } catch (error) {
            console.error('Error fetching tags:', error);
        }
    };

    const fetchUsers = async (query) => {
        try {
            const response = await searchForUsersRequest(query);
            setSearchResults(response.data);
        } catch (error) {
            console.error('Error fetching users:', error);
        }
    };

    const handleInputChange = (e) => {
        const value = e.target.value;
        setSearchString(value);
    };

    useEffect(() => {
        const timer = setTimeout(() => {
            setDebouncedSearchString(searchString);
        }, 400);

        return () => clearTimeout(timer);
    }, [searchString]);

    useEffect(() => {
        if (debouncedSearchString.startsWith('t/') && debouncedSearchString.length > 2) {
            fetchTags(debouncedSearchString.slice(2));
            setAreSearchResultsVisible(true);
        } else if (debouncedSearchString.startsWith('u/') && debouncedSearchString.length > 2) {
            fetchUsers(debouncedSearchString.slice(2));
            setAreSearchResultsVisible(true);
        } else {
            setAreSearchResultsVisible(false);
        }
    }, [debouncedSearchString]);

    const handleClickOutside = (e) => {
        if (
            searchResultsContainerRef.current &&
            !searchResultsContainerRef.current.contains(e.target) &&
            searchInputRef.current &&
            !searchInputRef.current.contains(e.target)
        ) {
            setAreSearchResultsVisible(false);
        }
    };

    useEffect(() => {
        document.addEventListener('click', handleClickOutside);
        return () => {
            document.removeEventListener('click', handleClickOutside);
        };
    }, []);

    const placeholderValue = feedType === 'home' || feedType === 'popular' ? '' :
        feedType === 'tag' ? `t/${param}` :
            feedType === 'user' ? `u/${param}` : '';

    return (
        <div className='dashboardHeaderContainer'>
            <div className='logoAndName'>
                <img
                    src='https://github.com/user-attachments/assets/fb215190-eb84-4f61-aa51-97ca581d6196'
                    alt='logo'
                    className='dashboardHeaderLogo'
                />
                <h2>PollIt</h2>
            </div>
            <div className='searchBar'>
                <input
                    type='text'
                    placeholder='Search'
                    className='searchInput'
                    ref={searchInputRef}
                    value={userHasTyped ? searchString : placeholderValue}
                    onChange={(e) => {
                        setUserHasTyped(true);
                        handleInputChange(e);
                    }}
                    onClick={() => {
                        if (!areSearchResultsVisible) setAreSearchResultsVisible(true);
                    }}
                />
                <div className='searchIconContainer'>
                    <img
                        className='searchIcon'
                        src='https://github.com/user-attachments/assets/21503eee-e069-4d5a-9559-836ac5639713'
                        alt='search icon'
                    />
                </div>
                {areSearchResultsVisible && debouncedSearchString.length > 0 && (
                    <div
                        className='searchResultsContainer'
                        ref={searchResultsContainerRef}
                    >
                        {searchResults.map((result) => (
                            <TopSearchBarResultComponent
                                key={result.id}
                                result={result}
                                typeOfSearch={debouncedSearchString.startsWith('t/') ? 'tag' : 'user'}
                                setAreSearchResultsVisible={setAreSearchResultsVisible}
                            />
                        ))}
                    </div>
                )}
            </div>
            <div className='profileIcon'>
                <img
                    src={userData.profilePictureURL}
                    alt='profile'
                    className='profileImage'
                    onClick={() => navigate(`/u/${userData.username}`)}
                />
            </div>
        </div>
    );
};

export default DashboardHeader;
