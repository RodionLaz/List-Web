import React, { useState } from 'react';
import axios, { AxiosResponse } from 'axios';
import { json } from 'stream/consumers';

function List() {
    const [editable, setEditable] = useState(false); 
    const [data, setData] = useState([
        {
            _id:"",
            title: "",
            mainText: ""
        },
    ]);

    const getList = async () => {
        try {
            const response = await axios.get("http://localhost:8080/GetList");
            console.log('Response data:', response.data);
            const parsedData = response.data.map((item: string) => JSON.parse(item));
            setData(parsedData);
            
        } catch (error) {
            console.error('Error:', error);
        }
    };

    const updateList = async () => {
  
        try {
            console.log("send update", data);
            const response: AxiosResponse = await axios.post("http://localhost:8080/UpdateList", data);
            console.log('Response data:', response);
        } catch (error) {
            console.error('Error:', error);
        }
    };

    const handleTextChange = (index: number, newText: string) => {
        const newData = data.map((item, idx) => {
            if (idx === index) {
                return {
                    ...item,
                    mainText: newText
                };
            }
            return item;
        });
        setData(newData);
    };
    const handleTitleTextChange = (index: number, newText: string) => {
        const newData = data.map((item, idx) => {
            if (idx === index) {
                return {
                    ...item,
                    title: newText
                };
            }
            return item;
        });
        setData(newData);
    };

    const handleEditList = () => {
        setEditable(!editable); 
    };
    const handleFinishEditing = () => {
        updateList();
    };
    return (
        <div className="list container">
            <h1>LIST</h1>
            <div className='buttons'>
                <button onClick={getList}>Get LIST</button>
                <button onClick={updateList}>Update List</button>
                <button>Remove List</button>
                <button onClick={handleEditList}>{editable ? 'Finish Editing' : 'Edit List'}</button>
            </div>
            <ul className='list'>
                {data.map((element, index) => (
                    <li key={index}>
                            {editable ? (
                            <input
                                type="text"
                                defaultValue={element.title}
                                onBlur={handleFinishEditing}
                                onChange={(e) => handleTitleTextChange(index, e.target.value)}
                            />
                        ) : (
                            <h3>{element.title}</h3>
                        )}
                        {editable ? (
                            <input
                                type="text"
                                defaultValue={element.mainText}
                                onBlur={handleFinishEditing}
                                onChange={(e) => handleTextChange(index, e.target.value)}
                            />
                        ) : (
                            element.mainText
                        )}
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default List;
