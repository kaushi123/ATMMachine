package com.atmmachine.service;

import com.atmmachine.dto.WithdrawalResponseDTO;
import com.atmmachine.entity.Account;
import com.atmmachine.entity.Notes;
import com.atmmachine.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private NoteService noteService;

    /**
     * Get the Balance of the Account
     *
     * @param accountNumber
     * @return Account
     */
    public Account getBalanceInfo(String accountNumber){
        Optional<Account>account =accountRepository.findById(accountNumber);
        if(!account.isPresent()){
            throw new IllegalArgumentException("No Account found for given Account Number");
        }
        return account.get();
    }


    /**
     * Execute the withdrawal request
     *
     * @param accountNumber,
     * @param amount,
     * @param allowOverDraft,
     * @return WithdrawalResponseDTO
     */
    public WithdrawalResponseDTO withdrawRequest(String accountNumber,int amount,boolean allowOverDraft ){
        String message = null;
        List<Notes>availableNotes = noteService.getAvailableNoteLimitsInATM();
        List<Notes>withdrawalCashList = noteService.createEmptyNotesList();
        WithdrawalResponseDTO withdrawalResponseDTO = new WithdrawalResponseDTO();
        Optional<Account>account =accountRepository.findById(accountNumber);
        message = validateTransaction(accountNumber,amount,allowOverDraft,account);
        if(message!=null){
            withdrawalResponseDTO.setMessage(message);
            withdrawalResponseDTO.setBalance(account.get().getBalance());
            withdrawalResponseDTO.setOverdraft(account.get().getOverdraft());
        }else{
           message = validateATMCashValues(amount,availableNotes);
            if(message!=null){
                withdrawalResponseDTO.setMessage(message);
                withdrawalResponseDTO.setBalance(account.get().getBalance());
                withdrawalResponseDTO.setOverdraft(account.get().getOverdraft());
            }else{
                withdrawalCashList =withdraw(amount,availableNotes,withdrawalCashList);
                updateAccountBalance(accountNumber, account.get().getBalance().subtract(BigDecimal.valueOf(amount)));
                Optional<Account>accountNew =accountRepository.findById(accountNumber);
                withdrawalResponseDTO.setWithdrawalList(withdrawalCashList);
                withdrawalResponseDTO.setMessage("Successful Transaction");
                withdrawalResponseDTO.setBalance(account.get().getBalance().subtract(BigDecimal.valueOf(amount)));
                withdrawalResponseDTO.setOverdraft(account.get().getOverdraft());
            }

        }
        Optional<Account>accountNew =accountRepository.findById(accountNumber);
        return withdrawalResponseDTO;
    }

    /**
     * Execute the withdrawal request logic and update the tables
     *
     * @param availableNotes,
     * @param amount,
     * @param withdrawalCashList,
     * @return List<Notes>
     */
    public List<Notes> withdraw(int amount, List<Notes> availableNotes, List<Notes> withdrawalCashList){

        checkForAvailabilityOfNotes(amount,availableNotes,withdrawalCashList);
        updateATMNotesCount(availableNotes,withdrawalCashList);

        return withdrawalCashList;
    }
    /**
     * Validate the ATM Cash Values
     *
     * @param availableNotes,
     * @param amount,
     * @return message
     */
    private String validateATMCashValues(int amount,List<Notes>availableNotes){
        int total = 0;
        String message = null;
        for(Notes note:availableNotes){
            total += note.getNoteCategory()*note.getQuantity();
        }
        if(total<0 || (total<amount)){
            message = "Insufficient Balance in the ATM";
        }
        return message;
    }
    /**
     * Validate the Account information which are related to the transaction
     *
     * @param allowOverDraft,
     * @param amount,
     * @param accountNumber,
     * @param account,
     * @return message
     */
    private String validateTransaction(String accountNumber,int amount,boolean allowOverDraft,Optional<Account>account){
        String message = null;
        if(amount<0){
           message ="Amount should be greater than 0";
        }
        if(!account.isPresent()){
            message ="No valid account found this Account Number";
        }
        if(!verifyBalanceForWithdraw(account.get(),amount,allowOverDraft)) {
            message = "Insufficient Balance in the account";
        }
        if (amount % 10 != 5 && amount % 10 != 0) {
            message = "The request can't be completed since this ATM doesn't hold bills smaller than €5";
        }

        return message;
    }


    /**
     * Validate the Account information which are related to the transaction by using useOverdraft value
     *
     * @param useOverdraft,
     * @param amount,
     * @param account,
     * @return boolean
     */
    private boolean verifyBalanceForWithdraw(Account account, int amount, boolean useOverdraft) {
        BigDecimal assumingBalanceAfterWithdraw = account.getBalance().subtract(BigDecimal.valueOf(amount));
        if (useOverdraft) {
            if (assumingBalanceAfterWithdraw.compareTo(account.getOverdraft().negate()) < 0) {
                return false;
            }
        } else {
            if (assumingBalanceAfterWithdraw.compareTo(BigDecimal.ZERO) < 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Create the withdrawal cash List
     *
     * @param availableNotes,
     * @param amount,
     * @param withdrawalCashList,
     * @return List<Notes>
     */
    private List<Notes> checkForAvailabilityOfNotes(int amount, List<Notes> availableNotes, List<Notes> withdrawalCashList) {
        int tempAmount = amount;
        for (int i = 0; i < availableNotes.size(); i++) {
            if (tempAmount == 0) {
                break;
            }

            Notes notes = availableNotes.get(i);
            int quantityAvailable = notes.getQuantity();
            int amountForComparison = notes.getNoteCategory();
            while (tempAmount >= amountForComparison && quantityAvailable > 0) {
                Notes withdraw = withdrawalCashList.get(i);
                withdraw.setQuantity(withdraw.getQuantity() + 1);
                quantityAvailable--;
                tempAmount -= amountForComparison;
            }
        }
        return withdrawalCashList;
    }

    /**
     * Update the ATM Values.
     *
     * @param availableNotes,
     * @param withdrawalCashList,
     */
    private void updateATMNotesCount(List<Notes> availableNotes, List<Notes> withdrawalCashList) {
        for (int i = 0; i < availableNotes.size(); i++) {
            availableNotes.get(i).setQuantity(
                    availableNotes.get(i).getQuantity() -
                            withdrawalCashList.get(i).getQuantity()
            );
        }
    }

    /**
     * Update the Account Values.
     *
     * @param accountNumber,
     * @param balance,
     */
    public void updateAccountBalance(String accountNumber, BigDecimal balance) {
        accountRepository.updateAccountBalance(accountNumber, balance);
    }
}
