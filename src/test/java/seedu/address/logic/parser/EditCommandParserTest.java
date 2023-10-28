package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.*;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalPatients.*;
import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPatientDescriptor;
import seedu.address.model.patient.*;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.EditPatientDescriptorBuilder;

public class EditCommandParserTest {

    private static final String TAG_EMPTY = " " + PREFIX_TAG;

    private static final String MESSAGE_INVALID_FORMAT = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
            EditCommand.MESSAGE_USAGE);

    private EditCommandParser parser = new EditCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no IC specified
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "edit i/", expectedMessage);
        //assertParseFailure(parser, VALID_NAME_AMY, MESSAGE_INVALID_FORMAT);
        // no field specified
        //assertParseFailure(parser, "edit i/T0000000A", EditCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        //assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidInputPrefix_failure() {
        // negative index
        assertParseFailure(parser, "edit i/-5" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "edit i/0" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "edit i/1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "edit i/string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "edit i/T1234567J" + INVALID_NAME_DESC, Name.MESSAGE_CONSTRAINTS); // invalid name
        assertParseFailure(parser, "edit i/T1234567J" + INVALID_PHONE_DESC, Phone.MESSAGE_CONSTRAINTS); // invalid phone
        assertParseFailure(parser, "edit i/T1234567J" + INVALID_EMAIL_DESC, Email.MESSAGE_CONSTRAINTS); // invalid email
        assertParseFailure(parser, "edit i/T1234567J" + INVALID_ADDRESS_DESC, Address.MESSAGE_CONSTRAINTS); // invalid address
        assertParseFailure(parser, "edit i/T1234567J" + INVALID_TAG_DESC, Tag.MESSAGE_CONSTRAINTS); // invalid tag

        // invalid phone followed by valid email
        assertParseFailure(parser, "edit i/T1234567J" + INVALID_PHONE_DESC + EMAIL_DESC_AMY, Phone.MESSAGE_CONSTRAINTS);

        // while parsing {@code PREFIX_TAG} alone will reset the tags of the {@code Patient} being edited,
        // parsing it together with a valid tag results in error
        assertParseFailure(parser, "edit i/T1234567J" + TAG_DESC_FRIEND + TAG_DESC_HUSBAND + TAG_EMPTY, Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "edit i/T1234567J" + TAG_DESC_FRIEND + TAG_EMPTY + TAG_DESC_HUSBAND, Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "edit i/T1234567J" + TAG_EMPTY + TAG_DESC_FRIEND + TAG_DESC_HUSBAND, Tag.MESSAGE_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, "edit i/T1234567J" + INVALID_NAME_DESC + INVALID_EMAIL_DESC + VALID_ADDRESS_AMY + VALID_PHONE_AMY,
                Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        IcNumber targetIc = AMY.getIcNumber();
        String userInput =
                "edit i/" + targetIc.toString() + PHONE_DESC_BOB + TAG_DESC_HUSBAND + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                        + NAME_DESC_AMY + TAG_DESC_FRIEND;

        EditPatientDescriptor descriptor = new EditPatientDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
                .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).withIcNumber(VALID_IC_NUMBER_AMY).build();
        EditCommand expectedCommand = new EditCommand(targetIc, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        IcNumber targetIc = AMY.getIcNumber();
        String userInput = "edit i/" + targetIc.toString() + PHONE_DESC_BOB + EMAIL_DESC_AMY;

        EditPatientDescriptor descriptor = new EditPatientDescriptorBuilder().withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_AMY).withIcNumber(VALID_IC_NUMBER_AMY).build();
        EditCommand expectedCommand = new EditCommand(targetIc, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // name
        IcNumber targetIc = CARL.getIcNumber();
        String userInput = "edit i/" + targetIc.toString() + NAME_DESC_AMY;
        EditCommand.EditPatientDescriptor descriptor = new EditPatientDescriptorBuilder().withName(VALID_NAME_AMY)
                .withIcNumber(targetIc.toString()).build();
        EditCommand expectedCommand = new EditCommand(targetIc, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // phone
        userInput = "edit i/" + targetIc.toString() + PHONE_DESC_AMY;
        descriptor = new EditPatientDescriptorBuilder().withPhone(VALID_PHONE_AMY).withIcNumber(targetIc.toString()).build();
        expectedCommand = new EditCommand(targetIc, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // email
        userInput = "edit i/" + targetIc.toString() + EMAIL_DESC_AMY;
        descriptor = new EditPatientDescriptorBuilder().withEmail(VALID_EMAIL_AMY).withIcNumber(targetIc.toString()).build();
        expectedCommand = new EditCommand(targetIc, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // address
        userInput = "edit i/" + targetIc.toString() + ADDRESS_DESC_AMY;
        descriptor = new EditPatientDescriptorBuilder().withAddress(VALID_ADDRESS_AMY).withIcNumber(targetIc.toString()).build();
        expectedCommand = new EditCommand(targetIc, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // tags
        userInput = "edit i/" + targetIc.toString() + TAG_DESC_FRIEND;
        descriptor = new EditPatientDescriptorBuilder().withTags(VALID_TAG_FRIEND).withIcNumber(targetIc.toString()).build();
        expectedCommand = new EditCommand(targetIc, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_failure() {
        // More extensive testing of duplicate parameter detections is done in
        // AddCommandParserTest#parse_repeatedNonTagValue_failure()

        // valid followed by invalid
        IcNumber targetIc = ALICE.getIcNumber();
        String userInput = "edit i/" + targetIc.toString() + PHONE_DESC_BOB + INVALID_PHONE_DESC; //+PHONE_DESC_BOB;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid followed by valid
        //userInput = "edit i/" + PHONE_DESC_BOB + INVALID_PHONE_DESC;

        //assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // multiple valid fields repeated
        userInput = "edit i/" + targetIc.toString() + PHONE_DESC_AMY + ADDRESS_DESC_AMY + EMAIL_DESC_AMY + TAG_DESC_FRIEND
                + PHONE_DESC_AMY + ADDRESS_DESC_AMY + EMAIL_DESC_AMY + TAG_DESC_FRIEND + PHONE_DESC_BOB + ADDRESS_DESC_BOB
                + EMAIL_DESC_BOB + TAG_DESC_HUSBAND;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS));

        // multiple invalid values
        userInput = "edit i/" + targetIc.toString() + INVALID_PHONE_DESC + INVALID_ADDRESS_DESC + INVALID_EMAIL_DESC
                + INVALID_PHONE_DESC + INVALID_ADDRESS_DESC + INVALID_EMAIL_DESC;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS));
    }

    @Test
    public void parse_resetTags_success() {
        IcNumber targetIc = CARL.getIcNumber();
        String userInput = "edit i/" + targetIc.toString() + TAG_EMPTY;

        EditPatientDescriptor descriptor = new EditPatientDescriptorBuilder().
                withTags().withIcNumber(targetIc.toString()).build();
        EditCommand expectedCommand = new EditCommand(targetIc, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
