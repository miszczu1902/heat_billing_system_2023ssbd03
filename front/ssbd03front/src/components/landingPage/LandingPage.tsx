import React from "react";
import {Container, Grid} from "@mui/material";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";

const LandingPage = () => {
    return(<div>
        <Container maxWidth="md">
            <Grid container>
                <Grid item xs={12} sm={6}>
                    <img src="https://via.placeholder.com/600x400" alt="Placeholder" />
                </Grid>
                <Grid item xs={12} sm={6}>
                    <Typography variant="h3">
                        Witaj na mojej stronie!
                    </Typography>
                    <Typography variant="body1">
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
                    </Typography>
                    <Button variant="contained" color="primary">
                        Więcej informacji
                    </Button>
                </Grid>
            </Grid>
        </Container>
    </div>);
}

export default LandingPage;