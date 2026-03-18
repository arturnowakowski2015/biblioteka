interface TableProps<T> {
    headers: (keyof T)[]; // Nagłówki to klucze obiektu T
    data: T[];            // Dane to tablica obiektów T
}

export const Table = <T,>({ headers, data }: TableProps<T>) => {
    return (
        <table>
            <thead>
                <tr>
                    {headers.map((key) => (
                        <th key={String(key)}>{String(key)}</th>
                    ))}
                </tr>
            </thead>
            <tbody>
                {data.map((row, i) => (
                    <tr key={i}>
                        {headers.map((key) => (
                            // Pobieramy wartość z wiersza 'row' pod kluczem 'key'
                            <td key={String(key)}>{String(row[key])}</td>
                        ))}
                    </tr>
                ))}
            </tbody>
        </table>
    );
};
