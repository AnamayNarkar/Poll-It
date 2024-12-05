import React from 'react'
import '../styles/AddedTagComponentStyles.css'

const AddedTagComponent = ({ removeTag, tag }) => {

    function deleteTag() {
        let newAllTagsArray = allTagsArray.filter((tagFromArray) => tagFromArray !== tag);
        setAllTagsArray(newAllTagsArray);
    }

    return (
        <div className='addedTagIndividual'>
            <p className='addedTagText'>
                {tag.name}</p>
            <img src='https://github.com/user-attachments/assets/f43e643f-143f-4974-8c62-d1373f3976cd' alt='delete' className='deleteAddedTagIcon' onClick={removeTag} />
        </div>
    )
}

export default AddedTagComponent
