import React from "react";
import {Container, Grid} from "@mui/material";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import {useNavigate} from "react-router-dom";

const LandingPage = () => {
    return (
        <div>
            <Container maxWidth="md">
                <Grid container spacing={3} alignItems="center" component="div">
                    <Grid item xs={12} sm={6}>
                    </Grid>
                    <Grid item xs={12} sm={6}>
                        <Typography variant="h3">
                            Witaj!
                        </Typography>
                        <Typography variant="body1">
                            To jest projekt dla rozliczenia ciepła dla lokali w wielu budynkach przygotowany przez zespół 3.
                        </Typography>
                        <Button variant="contained" color="primary">
                            ZAREJESTRUJ
                        </Button>
                        <Button variant="contained" color="primary">
                            ZALOGUJ SIĘ
                        </Button>
                    </Grid>
                </Grid>
            </Container>
        </div>
    );
}

export default LandingPage;