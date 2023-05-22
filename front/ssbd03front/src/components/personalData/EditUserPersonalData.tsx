import * as React from 'react';
import {useEffect, useState} from 'react';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import {TextField} from '@mui/material';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import axios from 'axios';
import validator from "validator";
import {API_URL} from '../../consts';
import {useCookies} from 'react-cookie';
import {useParams} from "react-router-dom";
import {useTranslation} from "react-i18next";

export default function EditUserPersonalData() {
    const {t, i18n} = useTranslation();
    const username = useParams().username;
    const [cookies, setCookie] = useCookies(["token", "etag"]);
    const token = "Bearer " + cookies.token;
    const etag = cookies.etag;
    const [version, setVersion] = useState("");

    const [open, setOpen] = useState(false);
    const [confirmOpen, setConfirmOpen] = useState(false);

    var [name, setName] = useState("");
    var [surname, setSurname] = useState("");

    var [nameError, setNameError] = useState("");
    var [surnameError, setSurnameError] = useState("");
    var [dataError, setDataError] = useState("");

    var [validData, setValidData] = useState(false);

    var [successOpen, setSuccessOpen] = useState(false);
    var [errorOpen, setErrorOpen] = useState(false);

    const [authorizationErrorOpen, setAuthorizationErrorOpen] = useState(false);

    const fetchData = async () => {
        const response = await axios.get(`${API_URL}/accounts/${username}/personal-data`, {
            headers: {
                Authorization: token
            }
        })
            .then(response => {
                setName(response.data.firstName.toString());
                setSurname(response.data.surname.toString());
                setCookie("etag", response.headers.etag);
                setVersion(response.data.version.toString());
            })
            .catch(error => {
                if (error.response.status === 403) {
                    setAuthorizationErrorOpen(true);
                    return;
                }
            });
    };

    useEffect(() => {
        fetchData();
    }, []);

    const handleSumbit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
    }

    const validateData = (event: React.ChangeEvent<HTMLInputElement>) => {
        if (nameError === "" && surnameError === "" && name !== "" && surname !== "" && event.target.value.length > 0) {
            setValidData(true);
        } else {
            setValidData(false);
        }
    }


    const handleNameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setName(event.target.value)
        if (validator.isAlpha(event.target.value) && event.target.value.length <= 32 && event.target.value.length > 0) {
            setNameError("");
            validateData(event);
        } else {
            setNameError(t('personal_data.name_error'));
            validateData(event);
        }
    };

    const handleSurnameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setSurname(event.target.value);
        if (validator.isAlpha(event.target.value) && event.target.value.length <= 32 && event.target.value.length > 0) {
            setSurnameError("");
            validateData(event);
        } else {
            setSurnameError(t('personal_data.surname_error'));
            validateData(event);
        }
    };

    const handleClickOpen = () => {
        fetchData();
        setOpen(true);
    };

    const handleClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setOpen(false);
        }
    };

    const handleConfirmClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setConfirmOpen(false);
        }
    }

    const handleConfirmConfirm = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setConfirmOpen(false);
        }
        const personalDataDTO = {
            firstName: name.toString(),
            surname: surname.toString(),
            version: version.toString()
        }

        if (nameError === "" && surnameError === "") {
            axios.patch(`${API_URL}/accounts/${username}/personal-data`,
                personalDataDTO, {
                    headers: {
                        'Authorization': token,
                        'Content-Type': 'application/json',
                        'If-Match': etag
                    },
                })
                .then(response => {
                    setSuccessOpen(true);
                })
                .catch(error => {
                    if (error.response.status === 403) {
                        setAuthorizationErrorOpen(true);
                        return;
                    }
                    setErrorOpen(true);
                });
        }
        handleClose(event, reason);
    }

    const handleConfirm = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (validData) {
            setDataError("");
            setConfirmOpen(true);
        } else {
            setDataError(t('edit_password.data_error'));
        }
    }

    const handleSuccessClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setSuccessOpen(false);
        }
        window.location.reload();
    }

    const handleErrorClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setErrorOpen(false);
        }
    };

    const handleAuthorizationErrorOpen = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setAuthorizationErrorOpen(false);
            handleConfirmClose(event, reason);
        }
    };

    return (
        <div>
            <div>
                <Button onClick={handleClickOpen} variant="contained">{t('personal_data.edit_data')}</Button>
            </div>
            <Dialog disableEscapeKeyDown open={open} onClose={handleClose}>
                <DialogTitle>{t('personal_data.edit_title')}{username}</DialogTitle>
                <DialogContent>
                    <Box sx={{display: 'flex', flexWrap: 'wrap'}}>
                        <form onSubmit={handleSumbit}>
                            <List component="nav" aria-label="mailbox folders">
                                <ListItem>
                                    <div className="form-group" onChange={handleNameChange}>
                                        <TextField
                                            id="outlined-helperText"
                                            label={t('personal_data.name')}
                                            defaultValue={name}
                                            helperText={t('personal_data.name_helper_text')}
                                        />
                                        <div className="form-group">
                                            {nameError}
                                        </div>
                                    </div>
                                </ListItem>
                                <ListItem>
                                    <div className="form-group" onChange={handleSurnameChange}>
                                        <TextField
                                            id="outlined-helperText"
                                            label={t('personal_data.surname')}
                                            defaultValue={surname}
                                            helperText={t('personal_data.name_helper_text')}
                                        />
                                        <div className="form-group">
                                            {surnameError}
                                        </div>
                                    </div>
                                </ListItem>
                            </List>
                            <div className="form-group">
                                {dataError}
                            </div>
                        </form>
                    </Box>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>{t('confirm.cancel')}</Button>
                    <Button onClick={handleConfirm} disabled={!validData}>{t('confirm.ok')}</Button>
                </DialogActions>
            </Dialog>


            <Dialog disableEscapeKeyDown open={confirmOpen} onClose={handleConfirmClose}>
                <DialogTitle>{t('personal_data.confirm_edit')}{username} ?</DialogTitle>
                <DialogActions>
                    <Button onClick={handleConfirmClose}>{t('confirm.no')}</Button>
                    <Button onClick={handleConfirmConfirm}>{t('confirm.yes')}</Button>
                </DialogActions>
            </Dialog>

            <Dialog disableEscapeKeyDown open={successOpen}>
                <DialogTitle>{t('personal_data.edit_success_one')}{username}{t('personal_data.edit_success_two')}</DialogTitle>
                <Button onClick={handleSuccessClose}>{t('confirm.ok')}</Button>
            </Dialog>

            <Dialog disableEscapeKeyDown open={errorOpen}>
                <DialogTitle>{t('personal_data.edit_error')}{username}</DialogTitle>
                <Button onClick={handleErrorClose}>{t('confirm.ok')}</Button>
            </Dialog>

            <Dialog disableEscapeKeyDown open={authorizationErrorOpen}>
                <DialogTitle>{t('disable_account.authorization_error')}</DialogTitle>
                <Button onClick={handleAuthorizationErrorOpen}>{t('confirm.ok')}</Button>
            </Dialog>
        </div>
    );
}