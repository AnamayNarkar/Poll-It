import React, { useState } from 'react';
import '../styles/CreateTagsContainerStyles.css';
import createTagRequest from '../../../services/ApiRequests/createTagRequest';

const CreateTagsContainer = () => {
    const [tagName, setTagName] = useState('');
    const [statusMessage, setStatusMessage] = useState('');

    const handleCreateTag = async () => {
        const response = await createTagRequest(tagName);

        if (response.status >= 200 && response.status < 300) {
            setStatusMessage('Tag created successfully');
        } else {
            setStatusMessage(response.data.message || 'An error occurred');
        }

        setTimeout(() => {
            setStatusMessage('');
        }, 3000);

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
            {statusMessage && statusMessage === 'Tag created successfully' ? (<div className='statusMessageForCreateTags' style={{ color: 'green' }}>{statusMessage}</div>) : (<div className='statusMessageForCreateTags' style={{ color: 'red' }}>{statusMessage}</div>)}
        </div>
    );
};

export default CreateTagsContainer;
