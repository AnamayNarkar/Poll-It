import React, { useState, useEffect } from 'react';
import '../styles/SearchForTagsContainerStyles.css';
import axios from 'axios';
import AddedTagComponent from './AddedTagComponent';
import SearchedUpTagComponent from './SearchedUpTagComponent';
import { useNavigate } from 'react-router-dom';
import searchForTagsRequest from '../../../services/ApiRequests/searchForTagsRequest';

const SearchForTagsContainer = ({ addTags }) => {
    const [searchString, setSearchString] = useState('');
    const [tags, setTags] = useState([]);
    const [typingTimeout, setTypingTimeout] = useState(null);

    const fetchTags = async (searchQuery) => {
        try {
            const response = await searchForTagsRequest(searchQuery);
            setTags(response.data);
        } catch (error) {
            console.error('Error fetching tags:', error);
        }
    };

    useEffect(() => {
        if (searchString) {
            if (typingTimeout) clearTimeout(typingTimeout);

            const timeout = setTimeout(() => {
                fetchTags(searchString);
            }, 300);
            setTypingTimeout(timeout);
        } else {
            setTags([]);
        }

        return () => clearTimeout(typingTimeout);
    }, [searchString]);

    return (
        <>
            <div className="searchForTagsContainer">
                <input
                    type="text"
                    placeholder="Search for tags"
                    className="searchForTagsInput"
                    value={searchString}
                    onChange={(e) => setSearchString(e.target.value)}
                />
                <div className='searchResultsForTagsContainer'>
                    {tags.length > 0 ? (
                        tags.map((tag) => (
                            <SearchedUpTagComponent key={tag.id} tag={tag} addTags={addTags} />
                        ))
                    ) : searchString.length > 0 ? (
                        <div className="searchForTagsNoSuggestions">No suggestions</div>
                    ) : null}
                </div>
            </div>
        </>
    );
};

export default SearchForTagsContainer;
