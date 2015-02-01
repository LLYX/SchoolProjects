"""
Process SQuEaL queries from the keyboard and print the results.
"""

import db_io
import squeal


def main():
    """ () -> NoneType

    Ask for queries from the keyboard; stop when empty line is received. For
    each query, process it and use db_io.print_csv to print the results.
    """

    # Write your main function body here.

    database = db_io.read_database()
    
    user_input = input("Input query: ")
    print()
    while user_input != '':
        processed_input = squeal.process_query(user_input)
        new_table = squeal.join_tables(database, processed_input['from'])
        if 'where' in processed_input.keys():
            new_table = squeal.filter_rows(new_table, processed_input['where'])
        new_table = squeal.select_columns(new_table, processed_input['select'])
        db_io.print_csv(new_table)
        print()
        user_input = input("Input query: ")
        print()

if __name__ == '__main__':
    main()

    


        
