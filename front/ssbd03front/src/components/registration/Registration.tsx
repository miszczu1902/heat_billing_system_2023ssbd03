import React, {useEffect, useState} from "react";
import validator from "validator";
import {Link, useNavigate} from "react-router-dom";
import axios from "axios";
import {API_URL} from "../../consts";
import Paper from "@mui/material/Paper";
import Typography from "@mui/material/Typography";
import TextField from "@mui/material/TextField";
import {createTheme, ThemeProvider} from "@mui/material/styles";
import {useCookies} from "react-cookie";
import {Grid, Box, Button} from "@mui/material";
import FormControl from "@mui/material/FormControl";
import InputLabel from "@mui/material/InputLabel";
import Select, {SelectChangeEvent} from "@mui/material/Select";
import OutlinedInput from "@mui/material/OutlinedInput";
import MenuItem from "@mui/material/MenuItem";
import {useForm} from "react-hook-form";
import {RegistrationForm} from "../../types/registrationForm";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";


const Registration = () => {
    const theme = createTheme();
    const regexEmail = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{1,10}$/;
    const regexLogin = /^[a-zA-Z0-9_]{6,16}$/;
    const regexPassword = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,32}$/;
    const phoneNumberRegex = /^[0-9]{9}$/;
    const languageRegex = /^(?=.*\b(EN|PL)\b).+$/;
    const {register, handleSubmit} = useForm<RegistrationForm>();

    const onSubmit = handleSubmit((data: RegistrationForm) => {
        console.log(email, username, firstName, password, language);
        console.log(data);
        if (data.firstName.length > 32 || data.firstName.length == 0) setRegisterError('Maksymalna długość imienia to 32 znaki');
        else if (data.surname.length > 32 || data.surname.length == 0) setRegisterError('Maksymalna długość nazwiska to 32 znaki');
        else if (!regexEmail.test(data.email)) setRegisterError('Email niepoprawny');
        else if (!regexLogin.test(data.username)) setRegisterError('Login musi składać się z 6-16 znaków i składać się z liter i cyfr');
        else if (!regexPassword.test(data.password)) setRegisterError('Hasło musi składać się z 8-32 znaków, jednej dużej litery, znak specjalny');
        else if (!regexPassword.test(data.repeatedPassword)) setRegisterError('Hasło musi składać się z 8-32 znaków, jednej dużej litery, znak specjalny');
        else if (data.repeatedPassword !== data.password) setRegisterError('Hasła się nie zgadzają');
        else if (!phoneNumberRegex.test(data.phoneNumber)) setRegisterError('Numer telefonu niepoprawny');
        else if (!languageRegex.test(data.language)) setRegisterError('Język niepoprawny');
        else {
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
                })
                .catch((error) => {
                    setRegisterError(error.response.data.message);
                });
        }
    });

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


    const handleSuccessClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setSuccessOpen(false);
        }
    }

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
        if (firstName.length > 32) setValidationInfo('Maksymalna długość imienia to 32 znaki');
    };

    const handleSurnameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const name = event.target.value;
        setSurname(name);
        if (surname.length > 32) setValidationInfo('Maksymalna długość nazwiska to 32 znaki');
    };

    const handleEmailChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const mail = event.target.value;
        setEmail(mail);
        if (!regexEmail.test(email)) setValidationInfo('Email niepoprawny');
    };

    const handleUsernameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const login = event.target.value;
        setUsername(login);
        if (!regexLogin.test(username)) setValidationInfo('Nazwa użytkownika może zaweriać: dużą, małą literę, cyfrę oraz znak _');
    };

    const handlePasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const passwd = event.target.value;
        setPassword(passwd);
        if (!regexPassword.test(password)) setValidationInfo('Hasło musi zawierać: dużą, małą literę, cyfrę, znak specjalny oraz mieć długość 8 znaków');
    };

    const handleConfirmPasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const passwd = event.target.value;
        setConfirmPassword(passwd);
        if (regexPassword.test(confirmPassword)) {
            if (confirmPassword !== password) setValidationInfo('Hasła się nie zgadzają');
        } else setValidationInfo('Hasło musi zawierać: dużą, małą literę, cyfrę, znak specjalny oraz mieć długość 8 znaków');
    };

    const handlePhoneNumberChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const phone = event.target.value;
        setPhoneNumber(phone);
        if (!phoneNumberRegex.test(phoneNumber)) setValidationInfo('Numer telefonu niepoprawny');
    };

    const handleLanguageChange = (event: SelectChangeEvent<typeof language>) => {
        const lang = event.target.value;
        setLanguage(lang);
        if (languageRegex.test(language)) setValidationInfo('Język niepoprawny');

    };

    const handleBlur = (event: React.ChangeEvent<HTMLInputElement>) => {
        // setValidationInfo('');
    };

    return (
        <ThemeProvider theme={theme}>
            <Grid container justifyContent="center" alignItems="center">
                <Grid my={2} item sm={8} md={5} component={Paper} elevation={6}>
                    <Box sx={{my: 30, display: 'flex', flexDirection: 'column', alignItems: 'center', padding: 'auto'}}>
                        <Typography variant="h5"> Zarejestruj się </Typography>
                        <Typography sx={{color: 'red'}}>{registerError}</Typography>
                        <Box component="form" onSubmit={onSubmit}>
                            <TextField fullWidth margin="normal" label="Imię"
                                       {...register('firstName')}
                                       value={firstName}
                                       helperText="Podaj imię" onChange={handleFirstNameChange}
                                       // onBlur={handleBlur as any}
                            />
                            <TextField fullWidth margin="normal" label="Nazwisko"
                                       {...register('surname')}
                                       value={surname}
                                       helperText="Podaj nazwisko" onChange={handleSurnameChange}
                                       // onBlur={handleBlur as any}
                            />
                            <TextField fullWidth margin="normal" label="Email" value={email} {...register("email")}
                                       helperText="Wprowadź email" onChange={handleEmailChange}
                                       // onBlur={handleBlur as any}
                            />
                            <TextField fullWidth margin="normal" label="Nazwa użytkownika"
                                       {...register('username')}
                                       value={username}
                                       helperText="Wprowadź nazwę użytkownika" onChange={handleUsernameChange}
                                       // onBlur={handleBlur as any}
                            />
                            <TextField fullWidth margin="normal" label="Wprowadź hasło"
                                       {...register('password')}
                                       type="password"
                                       helperText="Wprowadź hasło" onChange={handlePasswordChange}
                                       value={password}
                                       // onBlur={handleBlur as any}
                            />
                            <TextField fullWidth margin="normal" label="Potwierdź hasło"
                                       type="password" {...register('repeatedPassword')}
                                       helperText="Potwierdź hasło" onChange={handleConfirmPasswordChange}
                                       value={confirmPassword}
                                       // onBlur={handleBlur as any}
                            />
                            <TextField fullWidth margin="normal" label="Numer telefonu"
                                       type="text" {...register('phoneNumber')}
                                       helperText="Podaj numer telefonu" onChange={handlePhoneNumberChange}
                                       value={phoneNumber}
                                       // onBlur={handleBlur as any}
                            />
                            <Box component="form" sx={{display: 'flex', flexWrap: 'wrap'}}>
                                <FormControl sx={{m: 1, minWidth: 120}}>
                                    <InputLabel
                                        id="demo-dialog-select-label"
                                    >Język</InputLabel>
                                    <Select
                                        {...register('language')}
                                        labelId="demo-dialog-select-label"
                                        id="demo-dialog-select"
                                        value={language}
                                        onChange={handleLanguageChange}
                                        // onBlur={handleBlur as any}
                                        input={<OutlinedInput label="Język"/>}
                                    >
                                        <MenuItem value={'PL'}>Polski</MenuItem>
                                        <MenuItem value={'EN'}>Angielski</MenuItem>
                                    </Select>
                                </FormControl>
                                <span>{validationInfo}</span>
                            </Box>
                            <Button type="submit" fullWidth variant="contained">Zarejestuj</Button>
                            <Box sx={{my: 1, display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
                                Masz już konto? Zaloguj się
                            </Box>
                        </Box>
                    </Box>
                </Grid>
            </Grid>
            <Dialog disableEscapeKeyDown open={successOpen}>
                <DialogTitle>Konto zarjestrowane. Na podany adres email zostałą wysłana wiadomość z linkiem aktywacyjnym</DialogTitle>
                <Button onClick={handleSuccessClose}>Ok</Button>
            </Dialog>
        </ThemeProvider>
    );

}

export default Registration;