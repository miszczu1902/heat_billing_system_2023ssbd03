import * as React from 'react';
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
import {API_URL} from '../../consts';
import {useCookies} from 'react-cookie';
import {useTranslation} from "react-i18next";
import { useState } from 'react';

const ChangePhoneNumber = () => {
    const {t, i18n} = useTranslation();
    const [cookies] = useCookies(["token"]);
    const token = "Bearer " + cookies.token;
    const [etag, setEtag] = useState(false);
    const [version, setVersion] = useState("");
    const [open, setOpen] = useState(false);
    const [confirmOpen, setConfirmOpen] = useState(false);
    const [phoneNumber, setPhoneNumber] = useState("");

    const [phoneNumberError, setPhoneNumberError] = useState("");
    const [dataError, setDataError] = useState("");

    const [phoneNumberValid, setPhoneNumberValid] = useState(false);

    const [successOpen, setSuccessOpen] = useState(false);
    const [errorOpen, setErrorOpen] = useState(false);
    const [errorOpenMessage, setErrorOpenMessage] = useState("");

    const handleSumbit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
    }

    const handleNewPhoneNumber = (event: React.ChangeEvent<HTMLInputElement>) => {
        let phoneNumber = event.target.value;
        setPhoneNumber(phoneNumber);
        const regex = /^\d{9}$/;
        if (!regex.test(phoneNumber)) {
            setPhoneNumberError(t('change_phone_number.error'));
            setPhoneNumberValid(false);
        } else {
            setPhoneNumberError("");
            setPhoneNumberValid(true);
        }
    };

    const handleClickOpen = () => {
        const fetchData = async () => {
            await axios.get(`${API_URL}/accounts/self`, {
                headers: {
                    Authorization: token
                }
            })
                .then(response => {
                    setEtag(response.headers["etag"]);
                    setVersion(response.data.version)
                });
        };
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
        const changePhoneNumberDTO = {
            phoneNumber: phoneNumber.toString(),
            version: parseInt(version)
        }

        axios.patch(`${API_URL}/accounts/self/phone-number`,
            changePhoneNumberDTO, {
                headers: {
                    'Authorization': token,
                    'If-Match': etag,
                    'Content-Type': 'application/json'
                },
            })
            .then(response => {
                setSuccessOpen(true);
            })
            .catch(error => {
                setErrorOpenMessage(error.response.data.message)
                setErrorOpen(true);
            });
        handleClose(event, reason);
    }

    const handleConfirm = () => {
        if (phoneNumberValid) {
            setDataError("");
            setConfirmOpen(true);
        } else {
            setDataError(t('change_phone_number.data_error'));
        }
    }

    const handleSuccessClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        setPhoneNumber("");
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

    return (
        <div>
            <div>
                <Button onClick={handleClickOpen} variant="contained">{t('change_phone_number.title')}</Button>
            </div>
            <Dialog disableEscapeKeyDown open={open} onClose={handleClose}>
                <DialogTitle>{t('change_phone_number.form_title')}</DialogTitle>
                <DialogContent>
                    <Box sx={{display: 'flex', flexWrap: 'wrap'}}>
                        <form onSubmit={handleSumbit}>
                            <List component="nav" aria-label="mailbox folders">
                                <ListItem>
                                    <div className="form-group" onChange={handleNewPhoneNumber}>
                                        <TextField
                                            id="outlined-helperText"
                                            label={t('change_phone_number.new_number')}
                                            defaultValue={phoneNumber}
                                            type="phoneNumber"
                                            helperText={t('change_phone_number.new_number_help_text')}
                                        />
                                        <div className="form-group">
                                            {phoneNumberError}
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
                    <Button onClick={handleConfirm} disabled={!phoneNumberValid}>{t('confirm.ok')}</Button>
                </DialogActions>
            </Dialog>

            <Dialog disableEscapeKeyDown open={confirmOpen} onClose={handleConfirmClose}>
                <DialogTitle>{t('change_phone_number.confirm_changes')}</DialogTitle>
                <DialogActions>
                    <Button onClick={handleConfirmClose}>{t('confirm.no')}</Button>
                    <Button onClick={handleConfirmConfirm}>{t('confirm.yes')}</Button>
                </DialogActions>
            </Dialog>

            <Dialog disableEscapeKeyDown open={successOpen}>
                <DialogTitle>{t('change_phone_number.success')}</DialogTitle>
                <Button onClick={handleSuccessClose}>{t('confirm.ok')}</Button>
            </Dialog>

            <Dialog disableEscapeKeyDown open={errorOpen}>
                <DialogTitle>{errorOpenMessage}</DialogTitle>
                <Button onClick={handleErrorClose}>{t('confirm.ok')}</Button>
            </Dialog>
        </div>
    );
}

export default ChangePhoneNumber;