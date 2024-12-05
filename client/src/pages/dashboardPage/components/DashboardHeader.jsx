import React, { useEffect, useRef, useState } from 'react';
import '../styles/DashboardHeaderStyles.css';
import TopSearchBarResultComponent from './TopSearchBarResultComponent';
import searchForTagsRequest from '../../../services/ApiRequests/searchForTagsRequest';

const DashboardHeader = () => {
    const [areSearchResultsVisible, setAreSearchResultsVisible] = useState(false);
    const [searchResults, setSearchResults] = useState([]);
    const searchResultsContainerRef = useRef(null);
    const searchInputRef = useRef(null);
    const [searchString, setSearchString] = useState('');
    const [debouncedSearchString, setDebouncedSearchString] = useState('');

    const fetchTags = async (searchQuery) => {
        try {
            const response = await searchForTagsRequest(searchQuery);
            setSearchResults(response.data);
        } catch (error) {
            console.error('Error fetching tags:', error);
        }
    };

    const sampleResultsWhenSearchingForUsers = [
        { name: 'John Doe', id: '1' },
        { name: 'Jane Doe', id: '2' },
        { name: 'John Smith', id: '3' },
        { name: 'Jane Smith', id: '4' },
    ];

    const handleInputChange = (e) => {
        const value = e.target.value;
        setSearchString(value);
    };

    useEffect(() => {
        const handler = setTimeout(() => {
            setDebouncedSearchString(searchString);
        }, 400);

        return () => {
            clearTimeout(handler);
        };
    }, [searchString]);

    useEffect(() => {
        if (debouncedSearchString.startsWith('t/') && debouncedSearchString.length > 2) {
            fetchTags(debouncedSearchString.slice(2));
            setAreSearchResultsVisible(true);
        } else if (debouncedSearchString.startsWith('u/')) {
            setSearchResults(sampleResultsWhenSearchingForUsers);
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
                    onChange={handleInputChange}
                    ref={searchInputRef}
                    onClick={() => {
                        if (!areSearchResultsVisible) setAreSearchResultsVisible(true);
                    }}
                    value={searchString}
                />
                <div className='searchIconContainer'>
                    <img
                        className='searchIcon'
                        src='https://github.com/user-attachments/assets/21503eee-e069-4d5a-9559-836ac5639713'
                        alt='searchIcon'
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
                            />
                        ))}
                    </div>
                )}
            </div>
            <div className='profileIcon'>
                <img
                    src='https://github.com/user-attachments/assets/66ffe0b4-47fc-4344-a575-2627f4f9be6a'
                    alt='profile'
                    className='profileImage'
                />
            </div>
        </div>
    );
};

export default DashboardHeader;
