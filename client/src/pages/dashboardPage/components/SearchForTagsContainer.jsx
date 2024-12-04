import React, { useState, useEffect } from 'react';
import '../styles/SearchForTagsContainerStyles.css';
import axios from 'axios';
import AddedTagComponent from './AddedTagComponent';
import SearchedUpTagComponent from './SearchedUpTagComponent';
import { useNavigate } from 'react-router-dom';

const SearchForTagsContainer = ({ addedTags, setAddedTags }) => {
    const [searchString, setSearchString] = useState('');
    const [tags, setTags] = useState([]);
    const [typingTimeout, setTypingTimeout] = useState(null);
    const navigate = useNavigate(); // Place this here so it can be used in the component

    const fetchTags = async (searchQuery) => {
        try {
            const response = await axios.get(`http://localhost:3000/api/tag/getTagsLike/${searchQuery}`, {
                withCredentials: true
            });
            if (response.status >= 200 && response.status < 300) {
                setTags(response.data.data);
            } else if (response.status === 403) {
                window.alert('Your session has expired. Please log in again.');
                navigate('/auth');
            }
        } catch (error) {
            console.error('Error fetching tags:', error);
        }
    };

    useEffect(() => {
        if (searchString) {
            if (typingTimeout) clearTimeout(typingTimeout);

            const timeout = setTimeout(() => {
                fetchTags(searchString);
            }, 300); // Fetch after 300ms of inactivity
            setTypingTimeout(timeout);
        } else {
            setTags([]); // Clear suggestions if input is empty
        }

        return () => clearTimeout(typingTimeout); // Cleanup timeout on component unmount or searchString change
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
                            <SearchedUpTagComponent key={tag.id} tag={tag} addedTags={addedTags} setAddedTags={setAddedTags} />
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
