package br.com.finance.finance.service;

import br.com.finance.finance.dto.UserRecordDto;

public interface UserService {
    String registerUser(UserRecordDto userRecordDto);
    String updateUser(UserRecordDto userRecordDto);
}
