package com.compilercharisma.chameleonbusinessstudio.client;

import com.compilercharisma.chameleonbusinessstudio.utils.VendiaField;
import com.compilercharisma.chameleonbusinessstudio.utils.VendiaQueryBuilder;
import com.compilercharisma.chameleonbusinessstudio.utils.VendiaSort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.compilercharisma.chameleonbusinessstudio.exception.InvalidVendiaFieldException;

public class VendiaQueryBuilderTester {
    
    @Test
    public void getFieldString_beforeSettingColumns_returnsEmpty(){
        var sut = new VendiaQueryBuilder();
        var actual = removeWhiteSpace(sut.getFieldsString());
        Assertions.assertTrue(actual.startsWith("{"), "column string should start with '{'");
        Assertions.assertTrue(actual.endsWith("}"), "column string should end with '}'");
    }

    @Test
    public void getFieldString_afterSettingColumns_includesEachColumn(){
        var fields = new String[]{"foo", "bar"};
        var sut = new VendiaQueryBuilder()
            .select(fields);
        var actual = sut.getFieldsString();
        Assertions.assertTrue(actual.contains(fields[0]));
        Assertions.assertTrue(actual.contains(fields[1]));
    }

    @Test
    public void select_givenAnInvalidField_throwsAnException(){
        var sut = new VendiaQueryBuilder();
        Assertions.assertThrows(
            InvalidVendiaFieldException.class, 
            ()->sut.select("{isMalicious: true}")
        );
    }

    @Test
    public void getTypeString_beforeSettingType_throwsAnException(){
        var sut = new VendiaQueryBuilder();
        Assertions.assertThrows(Exception.class, ()->sut.getTypeString());
    }

    @Test
    public void getTypeString_afterSettingType_includesThatType(){
        var sut = new VendiaQueryBuilder();
        var expected = "ExampleType";
        var actual = sut.from(expected).getTypeString();
        Assertions.assertTrue(
            actual.contains(expected), 
            String.format("\"%s\" does not contain \"%s\", but it should", actual, expected)
        );
    }

    @Test
    public void from_givenAnInvalidTypeName_throwsAnException(){
        var sut = new VendiaQueryBuilder();
        Assertions.assertThrows(Exception.class, ()->sut.from("{isMalicious: true}"));
    }

    @Test
    public void where_givenAValidClause_createsAFilter(){
        var sut = new VendiaQueryBuilder();
        
        sut.where(new VendiaField("foo").contains("bar"));
        var actual = sut.getFilterString();

        Assertions.assertTrue(actual.contains("foo"));
        Assertions.assertTrue(actual.contains("bar"));
    }

    @Test
    public void orderBy_givenValidFields_createsASort(){
        var sut = new VendiaQueryBuilder();

        sut.orderBy(
            VendiaSort.by("foo"),
            VendiaSort.by("bar", false)
        );
        var actual = sut.getSortString();

        Assertions.assertTrue(actual.contains("foo"));
        Assertions.assertTrue(actual.contains("bar"));
    }

    private String removeWhiteSpace(String in){
        return in.replaceAll("\\s", "");
    }
}
