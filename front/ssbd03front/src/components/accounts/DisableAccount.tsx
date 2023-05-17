import * as React from 'react';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogTitle from '@mui/material/DialogTitle';
import axios from 'axios';
import { API_URL } from '../../consts';
import { useParams } from "react-router-dom";
import { useCookies } from 'react-cookie';
import {useTranslation} from "react-i18next";

const DisableAccount = () => {
    const {t, i18n} = useTranslation();
    const username = useParams().username;
    const [cookies, setCookie] = useCookies(["token", "etag"]);
    const token = "Bearer " + cookies.token;
    const etag = cookies.etag;
    const [version, setVersion] = React.useState("");
    const [enable, setEnable] = React.useState(false);

    const [open, setOpen] = React.useState(false);
    const [confirmOpen, setConfirmOpen] = React.useState(false);

    const [successOpen, setSuccessOpen] = React.useState(false);
    const [errorOpen, setErrorOpen] = React.useState(false);

    const [blockedUserOpen, setBlockedUserOpen] = React.useState(false);


    const fetchData = async () => {
      await axios.get(`${API_URL}/accounts/${username}`, {
        headers: {
          Authorization: token
        }
      })
      .then(response => {
        setCookie("etag", response.headers.etag);
        setVersion(response.data.version.toString());
        setEnable(response.data.isEnable);
      });
  };

  const disable = async () => {
    axios.patch(`${API_URL}/accounts/${username}/disable`, 
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
      if(!enable) {
        setBlockedUserOpen(true);
        return;
      }
      disable();
      handleConfirmClose(event, reason);
    }

    const handleSuccessClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
      if (reason !== 'backdropClick') {
        setSuccessOpen(false);
      }
    }

    const handleErrorClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
      if (reason !== 'backdropClick') {
        setErrorOpen(false);
      }
    };

    const handleBlockedUserOpen = (event: React.SyntheticEvent<unknown>, reason?: string) => {
      if (reason !== 'backdropClick') {
        setBlockedUserOpen(false);
        handleConfirmClose(event, reason);
      }
    };

    return (
        <div>
            <div>
                <Button onClick={handleClickOpen} variant="contained">{t('disable_account.disable')}</Button>
            </div>
            <Dialog disableEscapeKeyDown open={open}>
                <DialogTitle>{t('disable_account.disable_confirm')}{username}?</DialogTitle>
                <DialogActions>
                    <Button onClick={handleConfirmClose}>{t('confirm.no')}</Button>
                    <Button onClick={handleConfirmConfirm}>{t('confirm.yes')}</Button>
                </DialogActions>
            </Dialog>

            <Dialog disableEscapeKeyDown open={successOpen}>
                <DialogTitle>{t('disable_account.success_one')}{username}{t('disable_account.success_two')}</DialogTitle>
                <Button onClick={handleSuccessClose}>{t('confirm.ok')}</Button>
            </Dialog>

            <Dialog disableEscapeKeyDown open={errorOpen}>
                <DialogTitle>{t('disable_account.error')}{username}</DialogTitle>
                <Button onClick={handleErrorClose}>{t('confirm.ok')}</Button>
            </Dialog>

            <Dialog disableEscapeKeyDown open={blockedUserOpen}>
                <DialogTitle>{t('disable_account.blocked_user_one')}{username}{t('disable_account.blocked_user_two')}</DialogTitle>
                <Button onClick={handleBlockedUserOpen}>{t('confirm.ok')}</Button>
            </Dialog>
        </div>
    );
}

export default DisableAccount;