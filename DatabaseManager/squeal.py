""" Module squeal: table and database manipulation functions.

The meanings of "table" and "database" are as described in db_io.py.
"""

# Write your Cartesian product function and all your other helper functions
# here.

def cartesian_product(table1, table2):
    ''' (table, table) -> table

    Return a table which is the cartesian product of table1 and table2.         
    '''
    
    table1_headers = strip_headers_from_table(table1)
    table2_headers = strip_headers_from_table(table2)
    cart_prod_headers = table1_headers + table2_headers
    table1_rows = strip_rows_from_table(table1)
    table2_rows = strip_rows_from_table(table2)
    cart_prod_rows = []
    
    for e1 in table1_rows:
        for e2 in table2_rows:
            cart_prod_rows.append(e1 + e2)

    columns = table_rows_to_table_columns(cart_prod_rows, cart_prod_headers)

    return populate_table(cart_prod_headers, columns)

def join_tables(database, table_name_list):
    ''' (database, list of str) -> table

    Return a table consisting of the cartesian products of every table named in
    table_name_list.
    '''

    tables_to_be_joined = []
    
    if '*' in table_name_list:
        for t in database:
            tables_to_be_joined.append(database[t])
    else:
        for t in table_name_list:
            if t in database:
                tables_to_be_joined.append(database[t])

    base_table = tables_to_be_joined[0]
    i = 1
    while i <= len(tables_to_be_joined) - 1:
        base_table = cartesian_product(base_table, tables_to_be_joined[i])
        i += 1

    return base_table

def filter_rows(original_table, constraints):
    ''' (table, list of str) -> table

    Return a table which consists of the rows from original_table that are
    within the parameters of the constraints.
    '''

    table_headers = strip_headers_from_table(original_table)
    table_rows = strip_rows_from_table(original_table)
    
    for c in constraints:
        if '=' in c:
            op = '='
            table_rows = filter_by_operator(op, c, table_headers, table_rows)
        elif '>' in c:
            op = '>'
            table_rows = filter_by_operator(op, c, table_headers, table_rows)

    filtered_columns = table_rows_to_table_columns(table_rows, table_headers)

    return populate_table(table_headers, filtered_columns)
        
def select_columns(original_table, selected_headers):
    ''' (table, list of str) -> table

    Return a table which consists of those columns from original_table which
    have headers named in selected_headers.
    '''

    selected_columns = {}
    if '*' in selected_headers:
        selected_headers = [k for k in list(original_table.keys())]

    for h in selected_headers:
        selected_columns[h] = original_table[h]

    return selected_columns
    
#Helper functions below
    
def strip_headers_from_table(table):
    ''' (table) -> list of str

    Return a list of str consisting of the headers/titles of a given table.
    '''

    table_headers = []
    
    for k in table.keys():
        table_headers.append(k)

    return table_headers

def strip_rows_from_table(table):
    ''' (table) -> list of lists

    Return a list of lists where each element is an embedded list representing
    the individual rows of a given table.
    '''

    rows_list = []
    items_in_table = list(table.items())
    
    i = 0
    while i < len(items_in_table[0][1]):
        row = []
        for k in table:
            row.append(table[k][i])
        rows_list.append(row)
        i += 1

    return rows_list

def table_rows_to_table_columns(rows_list, headers):
    ''' (list of lists, list of str) -> list of lists

    Return a list of lists, where each element is an embedded list representing
    a column of a table.
    '''

    columns = []
    for h in headers:
        columns.append([])
        
    for e in rows_list:
        row_data = ','.join(e)
        row_data = row_data.split(',')
        i = 0
        for c in row_data:
            columns[i].append(c)
            i += 1

    return columns

def populate_table(headers, columns):
    ''' (list of str, list of lists) -> table

    Return a table with titles from headers and column data from columns.
    '''

    table = {}
    
    i = 0
    for h in headers:
        table[h] = columns[i]
        i += 1

    return table

def process_query(query):
    ''' (str) -> list of str

    Return a processed version of query, sorted by token keywords.
    '''
    
    processed_query = {}
    query_list = query.split(' ')

    i = 0
    while i + 1 <= len(query_list) - 1:
        processed_query[query_list.pop(i)] = query_list[i + 1].split(',')
        i += 1

    return processed_query

def filter_by_operator(operator, constraint, table_headers, table_rows):
    ''' (str, list of str, list) -> NoneType
    '''

    filtered_rows = []
    constraint = constraint.split(operator)
    if operator == '=':
        operator = '=='
    index1 = table_headers.index(constraint[0])
    if constraint[1] in table_headers:
        index2 = table_headers.index(constraint[1])
        for r in table_rows:
            if eval('{} {} {}'.format(r[index1], operator, r[index2])):
                filtered_rows.append(r)
    else:
        for r in table_rows:
            if eval("'{}' {} '{}'".format(r[index1], operator, constraint[1])):
                filtered_rows.append(r)

    return filtered_rows
    

