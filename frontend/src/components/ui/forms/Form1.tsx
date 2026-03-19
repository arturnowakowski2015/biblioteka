import Button from '../Buttons/Button';

export const MyForm = () => {
    return (
        <Button
            type="submit"
            variant="primary"
            onClick={() => console.log('Zapisano')}
        >
            <div></div>
            <span>Zapisz zmiany</span>
        </Button>
    );
};