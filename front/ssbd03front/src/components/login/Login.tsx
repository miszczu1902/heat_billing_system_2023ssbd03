import * as React from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Link from '@mui/material/Link';
import Paper from '@mui/material/Paper';
import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid';
import Typography from '@mui/material/Typography';
import {API_URL} from '../../consts';
import {createTheme, ThemeProvider} from '@mui/material/styles';
import axios from 'axios';
import {useCookies} from 'react-cookie';
import {useNavigate} from "react-router-dom";

const theme = createTheme();

const Login = () => {
    const navigate = useNavigate();
    const [cookies, setCookie] = useCookies(["token"]);
    const [username, setUsername] = React.useState("");
    const [password, setPassword] = React.useState("");
    const [loginError, setLoginError] = React.useState("");

    const handleUsernameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setUsername(event.target.value);
    };
    const handlePasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setPassword(event.target.value);
    };
    const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        if (username.length < 6) {
            setLoginError('Login musi mieć przynajmniej 6 znaków');
        } else if (password.length < 8) {
            setPassword("");
            setLoginError('Hasło musi mieć przynajmniej 8 znaków');
        } else if (username.length >= 16) {

            setLoginError('Login może mieć maksymalnie 16 znaków');
        } else {
            let data = JSON.stringify({
                "username": username,
                "password": password
            });

            let config = {
                method: 'post',
                maxBodyLength: Infinity,
                url: API_URL + '/accounts/login',
                headers: {
                    'Content-Type': 'application/json'
                },
                data: data,
            };
            axios.request(config)
                .then((response) => {
                    setCookie("token", response.headers["bearer"])
                    navigate('/accounts')
                })
                .catch((error) => {
                    setPassword("");
                    setLoginError(error.response.data.message);
                });
        }
    };

    return (
        <ThemeProvider theme={theme}>
            <Grid container justifyContent="center" alignItems="center">
                <Grid my={2} item sm={8} md={5} component={Paper} elevation={6}>
                    <Box sx={{my: 30, display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
                        <Typography variant="h5"> Logowanie </Typography>
                        <p>{loginError}</p>
                        <Box component="form" onSubmit={handleSubmit}>
                            <Box component="form">
                                <TextField fullWidth margin="normal" label="Login" value={username}
                                           helperText="Wprowadź Login" onChange={handleUsernameChange}/>
                                <TextField fullWidth margin="normal" label="Hasło" type="password"
                                           helperText="Wprowadź hasło" onChange={handlePasswordChange}
                                           value={password}/>
                            </Box>
                            <Button type="submit" fullWidth variant="contained">Zaloguj</Button>
                            <Box sx={{my: 1, display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
                                <Link href="????">Zapomniałeś hasła?</Link>
                            </Box>
                        </Box>
                    </Box>
                </Grid>
            </Grid>
        </ThemeProvider>
    );
}

export default Login;