import React, {useEffect, useState} from "react";
import {Link, useNavigate} from "react-router-dom";
import axios from "axios";
import {API_URL} from "../../consts";
import Paper from "@mui/material/Paper";
import Typography from "@mui/material/Typography";
import TextField from "@mui/material/TextField";
import {createTheme, ThemeProvider} from "@mui/material/styles";
import {useCookies} from "react-cookie";
import {Box, Button, Grid, Icon} from "@mui/material";
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
import {useTranslation} from "react-i18next";

const Registration = () => {
    const {t, i18n} = useTranslation();
    const theme = createTheme();
    const regexEmail = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{1,10}$/;
    const regexLogin = /^[a-zA-Z0-9_]{6,16}$/;
    const regexPassword = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,32}$/;
    const phoneNumberRegex = /^[0-9]{9}$/;
    const languageRegex = /^(?=.*\b(EN|PL)\b).+$/;

    const {register, handleSubmit} = useForm<RegistrationForm>();
    const navigate = useNavigate();
    const [cookies, setCookie] = useCookies(["token", "role"]);
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

    const [authorizationErrorOpen, setAuthorizationErrorOpen] = useState(false);

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
                if (error.response.status === 409) {
                    setAuthorizationErrorOpen(true);
                    return;
                }
                setRegisterError(error.response.data.message);
                if (error.response.status == 403) navigate('/');
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

    const handleAuthorizationErrorOpen = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setAuthorizationErrorOpen(false);
            handleConfirmClose(event, reason);
        }
    };

    const handleConfirm = () => {
        if (validateFirstName()
            && validateSurname()
            && validateEmail()
            && validateUsername()
            && validatePassword()
            && validateConfirmPassword()
            && validatePhoneNumber()
            && validateLanguage()) {
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

    const handleFirstNameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setFirstName(event.target.value);
        validateFirstName();
    };

    const handleSurnameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setSurname(event.target.value);
        validateSurname();
    };

    const handleEmailChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setEmail(event.target.value);
        validateEmail();
    };

    const handleUsernameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setUsername(event.target.value);
        validateUsername();
    };

    const handlePasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setPassword(event.target.value);
        validatePassword();
    };

    const handleConfirmPasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setConfirmPassword(event.target.value);
        validateConfirmPassword();
    };

    const handlePhoneNumberChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setPhoneNumber(event.target.value);
        validatePhoneNumber();
    };

    const handleLanguageChange = (event: SelectChangeEvent<typeof language>) => {
        setLanguage(event.target.value);
        validateLanguage();
    };

    const validateUsername = () => {
        console.log(username);
        if (!regexLogin.test(username)) {
            setValidationInfo(t('register.username_valid_info'));
            return false;
        } else {
            setValidationInfo('');
            return true;
        }
    };

    const validateEmail = () => {
        console.log(email);
        if (!regexEmail.test(email)) {
            setValidationInfo(t('register.email_error'));
            return false;
        } else {
            setValidationInfo('');
            return true;
        }
    };

    const validateFirstName = () => {
        console.log(firstName);
        if (firstName.length > 32 || firstName.length == 0) {
            setValidationInfo(t('register.first_name_error'));
            return false;
        } else {
            setValidationInfo('');
            return true;
        }
    };

    const validateSurname = () => {
        console.log(surname);
        if (surname.length > 32 || surname.length == 0) {
            setValidationInfo(t('register.surname_error'));
            return false;
        } else {
            setValidationInfo('');
            return true;
        }
    };

    const validatePassword = () => {
        console.log(password);
        if (!regexPassword.test(password)) {
            setValidationInfo(t('register.username_valid_info'));
            return false;
        } else {
            setValidationInfo('');
            return true;
        }
    };

    const validateConfirmPassword = () => {
        console.log(confirmPassword);
        if (regexPassword.test(confirmPassword)) {
            if (confirmPassword !== password) {
                setValidationInfo(t('register.passworde_error'));
                return false;
            } else {
                setValidationInfo('');
                return true;
            }
        } else {
            setValidationInfo(t('register.username_valid_info'));
            return false;
        }
    };

    const validatePhoneNumber = () => {
        console.log(phoneNumber);
        if (!phoneNumberRegex.test(phoneNumber)) {
            setValidationInfo(t('register.phone_number_error'));
            return false;
        } else {
            setValidationInfo('');
            return true;
        }
    };

    const validateLanguage = () => {
        console.log(language);
        if (!languageRegex.test(language)) {
            setValidationInfo(t('register.language_error'));
            return false;
        } else {
            setValidationInfo('');
            return true;
        }
    };

    return (
        <ThemeProvider theme={theme}>
            <Grid container justifyContent="center" alignItems="center"
                  sx={{background: '#1c8de4', height: '100vh', width: '100vw'}}>
                <Grid my={2} item sm={8} md={5} component={Paper} elevation={6}>
                    <Box component="form" sx={{
                        display: 'flex',
                        flexWrap: 'wrap',
                        alignItems: 'center',
                        justifyContent: 'center',
                        margin: '2vh'
                    }}>
                        <Typography sx={{padding: '1vh'}} variant="h5">{t('navbar.register')}</Typography>
                        <Icon sx={{width: '10%', height: '10%', marginLeft: '1vh'}}>
                            <img src={Logo}/>
                        </Icon>
                    </Box>
                    <Box sx={{my: 30, display: 'flex', flexDirection: 'column', alignItems: 'center', margin: '2vh'}}>
                        <Typography sx={{color: 'red'}}>{validationInfo}</Typography>
                        <Box component="form" onSubmit={onSubmit}>
                            <TextField fullWidth margin="normal" label={t('personal_data.name') + '*'}
                                       {...register('firstName')}
                                       value={firstName}
                                       helperText={t('register.set_name')}
                                       onChange={handleFirstNameChange}
                                       onFocus={handleFocus}
                            />
                            <TextField fullWidth margin="normal" label={t('personal_data.surname') + '*'}
                                       {...register('surname')}
                                       value={surname}
                                       helperText={t('register.set_surname')} onChange={handleSurnameChange}
                                       onFocus={handleFocus}
                            />
                            <TextField fullWidth margin="normal" label={t('register.email') + '*'}
                                       value={email} {...register("email")}
                                       helperText={t('register.set_email')} onChange={handleEmailChange}
                                       onFocus={handleFocus}
                            />
                            <TextField fullWidth margin="normal" label={t('login.username') + '*'}
                                       {...register('username')}
                                       value={username}
                                       helperText={t('register.set_username')} onChange={handleUsernameChange}
                                       onFocus={handleFocus}
                            />
                            <TextField fullWidth margin="normal" label={t('login.enter_password') + '*'}
                                       {...register('password')}
                                       type="password"
                                       helperText={t('login.enter_password')} onChange={handlePasswordChange}
                                       value={password}
                                       onFocus={handleFocus}
                            />
                            <TextField fullWidth margin="normal" label={t('register.password_confirm') + '*'}
                                       type="password" {...register('repeatedPassword')}
                                       helperText={t('register.password_confirm')}
                                       onChange={handleConfirmPasswordChange}
                                       value={confirmPassword}
                                       onFocus={handleFocus}
                            />
                            <Box component="form" sx={{
                                display: 'flex',
                                flexWrap: 'wrap',
                                alignItems: 'center',
                                justifyContent: 'center'
                            }}>
                                <TextField margin="normal" label={t('register.phone_number') + '*'}
                                           type="text" {...register('phoneNumber')}
                                           helperText={t('register.set_phone_number')}
                                           onChange={handlePhoneNumberChange}
                                           value={phoneNumber}
                                           onFocus={handleFocus}
                                />
                                <FormControl sx={{m: 1, minWidth: 120, marginBottom: 3}}>
                                    <InputLabel
                                        id="demo-dialog-select-label">{t('register.language') + '*'}</InputLabel>
                                    <Select
                                        {...register('language')}
                                        labelId="demo-dialog-select-label"
                                        id="demo-dialog-select"
                                        value={language}
                                        onChange={handleLanguageChange}
                                        onFocus={handleFocus}
                                        input={<OutlinedInput label="Język"/>}>
                                        <MenuItem value={'PL'}>{t('navbar.languages.pl')}</MenuItem>
                                        <MenuItem value={'EN'}>{t('navbar.languages.en')}</MenuItem>
                                    </Select>
                                </FormControl>
                                <Button sx={{marginBottom: 3}} onClick={handleConfirm}
                                        variant="contained">{t('navbar.register')}</Button>
                            </Box>
                            <Dialog disableEscapeKeyDown open={confirmOpen} onClose={handleSuccessClose}>
                                <DialogTitle>{t('register.register_confim')}</DialogTitle>
                                <DialogActions>
                                    <Button onClick={handleConfirmClose}>{t('confirm.no')}</Button>
                                    <Button type="submit" variant="contained"
                                            onClick={handleConfirmRegister}>{t('confirm.yes')}</Button>
                                </DialogActions>
                            </Dialog>
                            <Dialog disableEscapeKeyDown open={successOpen}>
                                <DialogTitle>{t('register.success')}</DialogTitle>
                                <Button onClick={handleSuccessClose}>{t('confirm.ok')}</Button>
                            </Dialog>
                            <Dialog disableEscapeKeyDown open={errorOpen}>
                                <DialogTitle>{registerError}</DialogTitle>
                                <Button onClick={handleErrorClose}>{t('confirm.ok')}</Button>
                            </Dialog>
                            <Box component="form" sx={{
                                display: 'flex',
                                flexWrap: 'wrap',
                                alignItems: 'center',
                                justifyContent: 'center'
                            }}>
                                <Link to='/login'>{t('register.log_in')}</Link>
                                <InputLabel sx={{marginLeft: '1vh'}}>{t('register.mandatory')}</InputLabel>
                            </Box>
                        </Box>
                    </Box>
                </Grid>
            </Grid>
            <Dialog disableEscapeKeyDown open={successOpen}>
                <DialogTitle>{t('register.success_two')}</DialogTitle>
                <Button onClick={handleSuccessClose}>{t('confirm.ok')}</Button>
            </Dialog>
            <Dialog disableEscapeKeyDown open={authorizationErrorOpen}>
                <DialogTitle>{t('register.user_exists')}</DialogTitle>
                <Button onClick={handleAuthorizationErrorOpen}>{t('confirm.ok')}</Button>
            </Dialog>
        </ThemeProvider>
    );
}

export default Registration;