"""Module db_io: functions for I/O on tables and databases.

A table file has a .csv extension.

We define "table" to mean this:

    dict of {str: list of str}

Each key is a column name from the table and each value is the list of strings
in that column from top row to bottom row.

We define "database" to mean this:

    dict of {str: table}

Each key is the name of the .csv file without the extension.  Each value is
the corresponding table as defined above.
"""

import glob


def print_csv(table):
    """ (table) -> NoneType

    Print a representation of table in the same format as a table file.
    """

    columns = list(table.keys())
    print(','.join(columns))

    # All columns are the same length; figure out the number of rows.
    num_rows = len(table[columns[0]])

    # Print each row in the table.
    for i in range(num_rows):

        # Build a list of the values in row i.
        curr_row = []
        for column_name in columns:
            curr_row.append(table[column_name][i])

        print(','.join(curr_row))


# Write your read_table and read_database functions below.
# Use glob.glob('*.csv') to return a list of csv filenames from
#   the current directory.

def read_table(file):
    ''' (file open for reading) -> table

    Return a table based on a correctly formatted open file for reading.
    '''

    headers = file.readline().split(',')
    headers[-1] = headers[-1].rstrip()
    columns = file_rows_to_table_columns(file, headers)

    return populate_table(headers, columns)

def read_database():
    ''' () -> database

    '''

    database = {}

    tables = glob.glob('*.csv')

    for t in tables:
        file = open(t, 'r')
        t = t.rstrip('csv').rstrip('.')
        k = read_table(file)
        database[t] = k

    return database

#Helper functions below

def file_rows_to_table_columns(file, headers):
    ''' (file open for reading, list of str) -> list of lists

    Return a list of lists consisting of the rows of a file open for reading
    sorted into columns. 
    '''

    columns = []
    for h in headers:
        columns.append([])
    
    for line in file:
        row_data = line.split(',')
        row_data[-1] = row_data[-1].rstrip()
        i = 0
        for c in row_data:
            columns[i].append(c)
            i += 1

    file.close()

    return columns

def populate_table(headers, columns):
    ''' (list of str, list of lists) -> table

    Return a table constructed with titles from headers and column data from
    columns.
    '''

    table = {}
    
    i = 0
    for h in headers:
        table[h] = columns[i]
        i += 1

    return table
