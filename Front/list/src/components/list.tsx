import React, { useState } from 'react';
import axios, { AxiosResponse } from 'axios';

function List() {
    const [editable, setEditable] = useState(false); 
    const [newRow,setNewRow] = useState(false);
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
        const isEmptyData = data.every((item) => {
            return Object.values(item).every((value) => value === "");
        });
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
    const createNewRow = (title:string,mainText:string) => {

        setData([...data, { _id: "", title: title, mainText:mainText }]);

        
    }
    const handleNewRowList = () => {
        setNewRow(!newRow); 
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
                <button>Remove Row</button>
                <button onClick={handleNewRowList}>{editable ? 'Finish Adding' : 'Add Row'}</button>
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
                            <div>{element.mainText}</div>
                        )}
                    </li>
                ))}
                <br></br>

                {newRow ? (
                            <input
                                type="text"
                                defaultValue={""}
                                onBlur={handleFinishEditing}
                                onChange={(e) => handleTitleTextChange(data.length-1, e.target.value)}
                            />
                        ) : (
                            <div></div>
                        )}
                        {newRow ? (
                            <input
                                type="text"
                                defaultValue={""}
                                onBlur={handleFinishEditing}
                                onChange={(e) => handleTextChange(data.length-1, e.target.value)}
                            />
                        ) : (
                            <div></div>
                        )}

            </ul>
        </div>
    );
}

export default List;
