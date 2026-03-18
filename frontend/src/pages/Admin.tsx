export const Admin = () => {
    return (

        <>Panel Administracyjny<section className="stats-grid">
            <div className="card card-blue">
                <span>Sprzedaż</span>
                <h3>142 500 PLN</h3>
                <small>+12% vs zeszły miesiąc</small>
            </div>
            <div className="card card-green">
                <span>Nowi Klienci</span>
                <h3>1,240</h3>
                <small>+5% wzrostu</small>
            </div>
            <div className="card card-orange">
                <span>Aktywne Sesje</span>
                <h3>482</h3>
                <small>Na żywo</small>
            </div>
        </section><table> <tbody>        <thead>
            <tr><th>ID</th><th>Klient</th><th>Stan</th><th>Kwota    </th>
            </tr>
        </thead>
            <tr>
                <td>#001</td>
                <td>Jan Kowalski</td>
                <td><span className="badge success">Zakończono</span></td>
                <td>1,200 PLN</td>
            </tr>
            <tr>
                <td>#002</td>
                <td>Anna Nowak</td>
                <td><span className="badge warning">Oczekuje</span></td>
                <td>850 PLN</td>
            </tr>
            <tr>
                <td>#003</td>
                <td>Piotr Wiśniewski</td>
                <td><span className="badge success">Zakończono</span></td>
                <td>2,100 PLN</td>
            </tr>
        </tbody>
            </table></>)
}