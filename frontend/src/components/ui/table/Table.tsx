import { useState } from "react";
import Button from "../Buttons/Button";

interface TableProps<T> {
    headers: (keyof T)[]; // Nagłówki to klucze obiektu T
    data?: T[];
    handleSubmit: (h: T) => void; // Dane to tablica obiektów T
}


export const Table = <T,>({ headers, data, handleSubmit }: TableProps<T>) => {
    const [editingRow, setEditingRow] = useState<number | null>(null);
    const [inputValue, setInputValue] = useState<Partial<T>>({});

    const handleSave = (row: T) => {
        handleSubmit({ ...row, ...inputValue });
        setEditingRow(null);
        setInputValue({});
    };

    return (
        <table>
            <thead>
                <tr>
                    {headers.map((key) => (
                        <th key={String(key)}>{String(key)}</th>
                    ))}
                    <th>Akcje</th>
                </tr>
            </thead>
            <tbody>
                {(Array.isArray(data) ? data : []).map((row, i) => (
                    <tr key={i}>
                        {headers.map((key) => (
                            editingRow === i
                                ? <td key={String(key)}>
                                    <input
                                        onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
                                            setInputValue({ ...inputValue, [key as string]: e.target.value })
                                        }
                                        value={String(inputValue[key] ?? row[key])}
                                    />
                                </td>
                                : <td key={String(key)} onClick={() => { setEditingRow(i); setInputValue({}); }}>
                                    {String(row[key])}
                                </td>
                        ))}
                        <td>
                            {editingRow === i
                                ? <Button type="submit" onClick={() => handleSave(row)}>
                                    <span>Zapisz zmiany</span>
                                </Button>
                                : <Button type="button" onClick={() => { setEditingRow(i); setInputValue({}); }}>
                                    <span>Edytuj</span>
                                </Button>
                            }
                        </td>
                    </tr>
                ))}
            </tbody>
        </table>
    );
};
