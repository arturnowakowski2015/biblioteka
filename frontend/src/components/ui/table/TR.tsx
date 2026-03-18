interface ListProps<T extends React.ReactNode> {
    items: T[];                // Tablica elementów dowolnego typu
    // Funkcja mówiąca, jak wyświetlić T
}



const TH = <T extends React.ReactNode,>({ items }: ListProps<T>) => {
    return (
        <thead>
            <tr>{
                items.map((t, i) => {
                    return <th key={i}>{t}</th>
                })
            }
            </tr>
        </thead>
    )
}

export default TH;