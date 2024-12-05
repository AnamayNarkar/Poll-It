import React, { useState } from 'react';
import '../styles/CreateTagsContainerStyles.css';
import axios from 'axios';

const CreateTagsContainer = () => {
    const [tagName, setTagName] = useState('');
    const [statusMessage, setStatusMessage] = useState('');

    const handleCreateTag = async () => {
        if (!tagName) {
            setStatusMessage('Please enter a tag name.');
            return;
        }

        tagName = tagName.trim();

        const response = await axios.post(`http://localhost:3000/api/tag/createTag/${tagName}`, {

        },
            { withCredentials: true },
            { headers: { 'Content-Type': 'application/json' } }
        );

        console.log('Response:', response.data);

        if (response.status >= 200 && response.status < 300) {
            setStatusMessage(response.data.message);
            setTagName('');
        } else {
            setStatusMessage(response.data.message);
        }
    };

    return (
        <div className='createTagsContainer'>
            <div className='inputAndButtonContainerForCreateTags'>
                <input
                    type='text'
                    placeholder='Create a tag'
                    className='inputForCreateTags'
                    value={tagName}
                    onChange={(e) => setTagName(e.target.value)}
                />
                <button className='buttonForCreateTags' onClick={handleCreateTag}>
                    Create
                </button>
            </div>
            {statusMessage && <div className='statusMessage'>{statusMessage}</div>}
        </div>
    );
};

export default CreateTagsContainer;
