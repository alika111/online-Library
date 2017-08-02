function checkValue(form, message){
    var input=form[form.id+":username"];
    if(input.value==''){
        alert(message)
        input.focus();
        return false; 
    }
    return true;
    
}
