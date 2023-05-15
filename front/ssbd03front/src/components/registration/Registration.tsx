import React, {useEffect, useState} from "react";
import {Link, useNavigate} from "react-router-dom";
import axios from "axios";
import {API_URL} from "../../consts";
import Paper from "@mui/material/Paper";
import Typography from "@mui/material/Typography";
import TextField from "@mui/material/TextField";
import {createTheme, ThemeProvider} from "@mui/material/styles";
import {useCookies} from "react-cookie";
import {Grid, Box, Button, Icon} from "@mui/material";
import FormControl from "@mui/material/FormControl";
import InputLabel from "@mui/material/InputLabel";
import Select, {SelectChangeEvent} from "@mui/material/Select";
import OutlinedInput from "@mui/material/OutlinedInput";
import MenuItem from "@mui/material/MenuItem";
import {useForm} from "react-hook-form";
import {RegistrationForm} from "../../types/registrationForm";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import Logo from './../../assets/logo.svg';
import DialogActions from "@mui/material/DialogActions";

const Registration = () => {
    const theme = createTheme();
    const regexEmail = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{1,10}$/;
    const regexLogin = /^[a-zA-Z0-9_]{6,16}$/;
    const regexPassword = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,32}$/;
    const phoneNumberRegex = /^[0-9]{9}$/;
    const languageRegex = /^(?=.*\b(EN|PL)\b).+$/;

    const {register, handleSubmit} = useForm<RegistrationForm>();
    const navigate = useNavigate();
    const [cookies, setCookie] = useCookies(["token"]);
    const [firstName, setFirstName] = useState('');
    const [surname, setSurname] = useState('');
    const [email, setEmail] = useState("");
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [phoneNumber, setPhoneNumber] = useState("");
    const [language, setLanguage] = useState<string>('');
    const [validationInfo, setValidationInfo] = useState('');
    const [registerError, setRegisterError] = useState("");
    const [loading, setLoading] = useState(true);
    const [loggedIn, setLoggedIn] = useState(false);
    const [successOpen, setSuccessOpen] = useState(false);
    const [errorOpen, setErrorOpen] = useState(false);
    const [confirmOpen, setConfirmOpen] = useState(false);

    const onSubmit = handleSubmit((data: RegistrationForm) => {
        let config = {
            method: 'post',
            maxBodyLength: Infinity,
            url: API_URL + '/accounts/register',
            headers: {
                'Content-Type': 'application/json'
            },
            data: JSON.stringify(data),
        };
        axios.request(config)
            .then((response) => {
                setSuccessOpen(true);
            })
            .catch((error) => {
                setRegisterError(error.response.data.message);
            });
    });

    useEffect(() => {
        if (cookies.token) {
            setLoggedIn(true);
        }
        setLoading(false);
    }, [cookies]);

    if (loading) {
        return <p></p>;
    }

    if (loggedIn) {
        navigate("/");
        return null;
    }

    const handleFirstNameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const name = event.target.value;
        setFirstName(name);
        if (event.target.value.length > 32) setValidationInfo('Maksymalna długość imienia to 32 znaki');
        else setValidationInfo('');
    };

    const handleSurnameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const name = event.target.value;
        setSurname(name);
        if (event.target.value.length > 32) setValidationInfo('Maksymalna długość nazwiska to 32 znaki')
        else setValidationInfo('');
    };

    const handleEmailChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const mail = event.target.value;
        setEmail(mail);
        if (!regexEmail.test(event.target.value)) setValidationInfo('Email niepoprawny');
        else setValidationInfo('');
    };

    const handleUsernameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const login = event.target.value;
        setUsername(login);
        if (!regexLogin.test(event.target.value)) setValidationInfo('Nazwa użytkownika może zaweriać: dużą, małą literę, cyfrę oraz znak _');
        else setValidationInfo('');
    };

    const handlePasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const passwd = event.target.value;
        setPassword(passwd);
        if (!regexPassword.test(event.target.value)) setValidationInfo('Hasło musi zawierać: dużą, małą literę, cyfrę, znak specjalny oraz mieć długość 8 znaków');
        else setValidationInfo('');
    };

    const handleConfirmPasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const passwd = event.target.value;
        setConfirmPassword(passwd);
        if (regexPassword.test(event.target.value)) {
            if (event.target.value !== password) setValidationInfo('Hasła się nie zgadzają');
            else setValidationInfo('');
        } else setValidationInfo('Hasło musi zawierać: dużą, małą literę, cyfrę, znak specjalny oraz mieć długość 8 znaków');
    };

    const handlePhoneNumberChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const phone = event.target.value;
        setPhoneNumber(phone);
        if (!phoneNumberRegex.test(event.target.value)) setValidationInfo('Numer telefonu niepoprawny');
        else setValidationInfo('');
    };

    const handleLanguageChange = (event: SelectChangeEvent<typeof language>) => {
        const lang = event.target.value;
        setLanguage(lang);
        if (languageRegex.test(event.target.value)) setValidationInfo('Język niepoprawny');
        else setValidationInfo('');
    };

    const handleFocus = () => {
        setValidationInfo('');
    };

    const handleConfirmClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setSuccessOpen(false);
        }
    }

    const handleConfirmRegister = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setConfirmOpen(false);
        }
        onSubmit();
    };

    const handleConfirm = () => {
        if (firstName.length > 32 || firstName.length == 0) setRegisterError('Maksymalna długość imienia to 32 znaki');
        else if (surname.length > 32 || surname.length == 0) setRegisterError('Maksymalna długość nazwiska to 32 znaki');
        else if (!regexEmail.test(email)) setRegisterError('Email niepoprawny');
        else if (!regexLogin.test(username)) setRegisterError('Login musi składać się z 6-16 znaków i składać się z liter i cyfr');
        else if (!regexPassword.test(password)) setRegisterError('Hasło musi składać się z 8-32 znaków, jednej dużej litery, znak specjalny');
        else if (!regexPassword.test(confirmPassword)) setRegisterError('Hasło musi składać się z 8-32 znaków, jednej dużej litery, znak specjalny');
        else if (confirmPassword !== password) setRegisterError('Hasła się nie zgadzają');
        else if (!phoneNumberRegex.test(phoneNumber)) setRegisterError('Numer telefonu niepoprawny');
        else if (!languageRegex.test(language)) setRegisterError('Język niepoprawny');
        else {
            setConfirmOpen(true);
        }
    }

    const handleSuccessClose = () => {
        navigate('/login');
    }

    const handleErrorClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setErrorOpen(false);
        }
    };

    return (
        <ThemeProvider theme={theme}>
            <Grid container justifyContent="center" alignItems="center" sx={{background: '#1c8de4', height: '100vh', width: '100vw'}}>
                <Grid my={2} item sm={8} md={5} component={Paper} elevation={6}>
                    <Box component="form" sx={{
                        display: 'flex',
                        flexWrap: 'wrap',
                        alignItems: 'center',
                        justifyContent: 'center',
                        margin: '2vh'
                    }}>
                        <Typography sx={{padding: '1vh'}} variant="h5"> Zarejestruj się</Typography>
                        <Icon sx={{width: '10%', height: '10%', marginLeft: '1vh'}}>
                            <img src={Logo}/>
                        </Icon>
                    </Box>
                    <Box sx={{my: 30, display: 'flex', flexDirection: 'column', alignItems: 'center', margin: '2vh'}}>
                        <Typography sx={{color: 'red'}}>{validationInfo}</Typography>
                        <Box component="form" onSubmit={onSubmit}>
                            <TextField fullWidth margin="normal" label="Imię"
                                       {...register('firstName')}
                                       value={firstName}
                                       helperText="Podaj imię" onChange={handleFirstNameChange}
                                       onFocus={handleFocus}
                            />
                            <TextField fullWidth margin="normal" label="Nazwisko"
                                       {...register('surname')}
                                       value={surname}
                                       helperText="Podaj nazwisko" onChange={handleSurnameChange}
                                       onFocus={handleFocus}
                            />
                            <TextField fullWidth margin="normal" label="Email" value={email} {...register("email")}
                                       helperText="Wprowadź email" onChange={handleEmailChange}
                                       onFocus={handleFocus}
                            />
                            <TextField fullWidth margin="normal" label="Nazwa użytkownika"
                                       {...register('username')}
                                       value={username}
                                       helperText="Wprowadź nazwę użytkownika" onChange={handleUsernameChange}
                                       onFocus={handleFocus}
                            />
                            <TextField fullWidth margin="normal" label="Wprowadź hasło"
                                       {...register('password')}
                                       type="password"
                                       helperText="Wprowadź hasło" onChange={handlePasswordChange}
                                       value={password}
                                       onFocus={handleFocus}
                            />
                            <TextField fullWidth margin="normal" label="Potwierdź hasło"
                                       type="password" {...register('repeatedPassword')}
                                       helperText="Potwierdź hasło" onChange={handleConfirmPasswordChange}
                                       value={confirmPassword}
                                       onFocus={handleFocus}
                            />
                            <Box component="form" sx={{
                                display: 'flex',
                                flexWrap: 'wrap',
                                alignItems: 'center',
                                justifyContent: 'center'
                            }}>
                                <TextField margin="normal" label="Numer telefonu"
                                           type="text" {...register('phoneNumber')}
                                           helperText="Podaj numer telefonu" onChange={handlePhoneNumberChange}
                                           value={phoneNumber}
                                           onFocus={handleFocus}
                                />
                                <FormControl sx={{m: 1, minWidth: 120, marginBottom: 3}}>
                                    <InputLabel id="demo-dialog-select-label">Język</InputLabel>
                                    <Select
                                        {...register('language')}
                                        labelId="demo-dialog-select-label"
                                        id="demo-dialog-select"
                                        value={language}
                                        onChange={handleLanguageChange}
                                        onFocus={handleFocus}
                                        input={<OutlinedInput label="Język"/>}>
                                        <MenuItem value={'PL'}>Polski</MenuItem>
                                        <MenuItem value={'EN'}>Angielski</MenuItem>
                                    </Select>
                                </FormControl>
                                <Button sx={{marginBottom: 3}} onClick={handleConfirm} variant="contained">Zarejestuj</Button>
                            </Box>
                            <Dialog disableEscapeKeyDown open={confirmOpen} onClose={handleSuccessClose}>
                                <DialogTitle>Czy na pewno chcesz się zarejestrować?</DialogTitle>
                                <DialogActions>
                                    <Button onClick={handleConfirmClose}>Nie</Button>
                                    <Button type="submit" variant="contained"
                                            onClick={handleConfirmRegister}>Tak</Button>
                                </DialogActions>
                            </Dialog>
                            <Dialog disableEscapeKeyDown open={successOpen}>
                                <DialogTitle>Konto zarejestrowane</DialogTitle>
                                <Button onClick={handleSuccessClose}>Ok</Button>
                            </Dialog>
                            <Dialog disableEscapeKeyDown open={errorOpen}>
                                <DialogTitle>{registerError}</DialogTitle>
                                <Button onClick={handleErrorClose}>Ok</Button>
                            </Dialog>
                            <Box component="form" sx={{
                                display: 'flex',
                                flexWrap: 'wrap',
                                alignItems: 'center',
                                justifyContent: 'center'
                            }}>
                                <Link to='/login'>Masz już konto? Zaloguj się</Link>
                            </Box>
                        </Box>
                    </Box>
                </Grid>
            </Grid>
            <Dialog disableEscapeKeyDown open={successOpen}>
                <DialogTitle>Konto zarjestrowane. Na podany adres email została wysłana wiadomość z linkiem
                    aktywacyjnym</DialogTitle>
                <Button onClick={handleSuccessClose}>Ok</Button>
            </Dialog>
        </ThemeProvider>
    );
}

export default Registration;