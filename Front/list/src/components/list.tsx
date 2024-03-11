import React, { useState } from 'react';
import axios, { AxiosResponse } from 'axios';

interface ListItem {
    _id: string;
    title: string;
    mainText: string;
}

function List() {
    const [editable, setEditable] = useState(false);
    const [newRow, setNewRow] = useState(false);
    const [data, setData] = useState<ListItem[]>([]);

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
    const deleteLast = async () =>{
        try{
        const newData = data.slice(0, -1);
        setData(newData);
            const response:AxiosResponse = await axios.delete("http://localhost:8080/DeleteLast");
            console.log(response);
        }catch(error){
            console.error(error);
        }
        

    } 

    const handleTextChange = (index: number, newText: string) => {
        const newData = [...data];
        newData[index].mainText = newText;
        setData(newData);

    };
    
    const handleTitleTextChange = (index: number, newText: string) => {
        const newData = [...data];
        newData[index].title = newText;
        setData(newData);
        
    };
    const handleNewRowList = () => {
        setData([...data, { _id: "", title: "", mainText: "" }]);
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
                <button onClick={deleteLast}>Remove Row</button>
                <button onClick={handleNewRowList}>{'Add Row'}</button>
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
            </ul>
        </div>
    );
}

export default List;
