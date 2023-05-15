import {Button, Container, Grid, Typography} from "@mui/material";
import {useNavigate} from "react-router-dom";
import logo from './../../assets/logo.svg';

const LandingPage = () => {
    const navigate = useNavigate();

    const handleButtonClick = (path: string) => {
        navigate(path);
    }

    return (
        <div className="landing-page-root">
            <Container>
                <img src={logo} alt="Logo"/>
            </Container>
            <Container maxWidth="sm">
                <Grid container direction="column" alignItems="center" spacing={4}>
                    <Grid item>
                        <Typography variant="h4" component="h1">
                            Projekt zespołu 3 SSBD 2023
                        </Typography>
                    </Grid>
                    <Grid item>
                        <Typography variant="subtitle1">
                            System rozliczania ciepła w lokalach w wielu budynkach.
                        </Typography>
                    </Grid>
                    <Grid item>
                        <Button className="landing-page-button" variant="contained" color="primary"
                                onClick={() => handleButtonClick('/register')}>
                            Zarejestruj się
                        </Button>
                        <Button className="landing-page-button" variant="contained" color="primary"
                                onClick={() => handleButtonClick('/login')}>
                            Zaloguj się
                        </Button>
                    </Grid>
                </Grid>
            </Container>
        </div>
    );
}

export default LandingPage;