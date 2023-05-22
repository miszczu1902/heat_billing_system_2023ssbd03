import * as React from 'react';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogTitle from '@mui/material/DialogTitle';
import axios from 'axios';
import {API_URL} from '../../consts';
import {useParams} from "react-router-dom";
import {useCookies} from 'react-cookie';
import {useTranslation} from "react-i18next";
import { useState } from 'react';

const EnableAccount = () => {
    const {t, i18n} = useTranslation();
    const username = useParams().username;
    const [cookies, setCookie] = useCookies(["token", "etag"]);
    const token = "Bearer " + cookies.token;
    const etag = cookies.etag;
    const [version, setVersion] = useState("");
    const [enableState, setEnable] = useState(false);

    const [open, setOpen] = useState(false);
    const [confirmOpen, setConfirmOpen] = useState(false);

    const [successOpen, setSuccessOpen] = useState(false);
    const [errorOpen, setErrorOpen] = useState(false);

    const [unblockedUserOpen, setUnblockedUserOpen] = useState(false);

    const [authorizationErrorOpen, setAuthorizationErrorOpen] = useState(false);

    const fetchData = async () => {
        await axios.get(`${API_URL}/accounts/${username}`, {
            headers: {
                Authorization: token
            }
        })
            .then(response => {
                setCookie("etag", response.headers.etag);
                setVersion(response.data.version);
                setEnable(response.data.isEnable);
            })
            .catch(error => {
                if (error.response.status === 403) {
                    setAuthorizationErrorOpen(true);
                    return;
                }
            });
    };

    const enable = async () => {
        axios.patch(`${API_URL}/accounts/${username}/enable`,
            {
                version: version
            },
            {
                headers: {
                    'Authorization': token,
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
    };

    const handleClickOpen = () => {
        fetchData();
        setOpen(true);
    };

    const handleConfirmClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setOpen(false);
        }
    }

    const handleConfirmConfirm = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setConfirmOpen(false);
        }
        if (enableState) {
            setUnblockedUserOpen(true);
            return;
        }
        enable();
        handleConfirmClose(event, reason);
    }

    const handleSuccessClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setSuccessOpen(false);
            window.location.reload();
        }
    }

    const handleErrorClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setErrorOpen(false);
        }
    };

    const handleUnblockedUserOpen = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setUnblockedUserOpen(false);
            handleConfirmClose(event, reason);
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
                <Button onClick={handleClickOpen} variant="contained">{t('enable_account.enable')}</Button>
            </div>
            <Dialog disableEscapeKeyDown open={open}>
                <DialogTitle>{t('enable_account.enable_confirm')}{username}?</DialogTitle>
                <DialogActions>
                    <Button onClick={handleConfirmClose}>{t('confirm.no')}</Button>
                    <Button onClick={handleConfirmConfirm}>{t('confirm.yes')}</Button>
                </DialogActions>
            </Dialog>

            <Dialog disableEscapeKeyDown open={successOpen}>
                <DialogTitle>{t('enable_account.success_one')}{username}{t('enable_account.success_two')}</DialogTitle>
                <Button onClick={handleSuccessClose}>{t('confirm.ok')}</Button>
            </Dialog>

            <Dialog disableEscapeKeyDown open={errorOpen}>
                <DialogTitle>{t('enable_account.error')}{username}</DialogTitle>
                <Button onClick={handleErrorClose}>{t('confirm.ok')}</Button>
            </Dialog>

            <Dialog disableEscapeKeyDown open={unblockedUserOpen}>
                <DialogTitle>{t('enable_account.unblocked_user_one')}{username}{t('enable_account.unblocked_user_two')}</DialogTitle>
                <Button onClick={handleUnblockedUserOpen}>{t('confirm.ok')}</Button>
            </Dialog>

            <Dialog disableEscapeKeyDown open={authorizationErrorOpen}>
                <DialogTitle>{t('enable_account.authorization_error')}</DialogTitle>
                <Button onClick={handleAuthorizationErrorOpen}>{t('confirm.ok')}</Button>
            </Dialog>
        </div>
    );
}
export default EnableAccount;