interface ListProps<T extends React.ReactNode> {
    items: T[];                // Tablica elementów dowolnego typu
    keys: (item: T) => React.Key; // Funkcja mówiąca, jak wyświetlić T
}



const TH = <T extends React.ReactNode,>({ items, keys }: ListProps<T>) => {
    return (
        <thead>
            <tr>{
                items.map((t, i) => {
                    return <th key={keys(t)}>{t}</th>
                })
            }
            </tr>
        </thead>
    )
}
export default TH;