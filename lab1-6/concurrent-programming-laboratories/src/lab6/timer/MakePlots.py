import pandas as pd
import matplotlib.pyplot as plt
from tabulate import tabulate

# Wczytaj dane z pliku CSV
df = pd.read_csv('../../../dane.csv')

for prod_cons in [5, 20]:
    for opers in [10000, 100000, 1000000, 10000000]:
        # Filtruj dane dla producers_consmers=5 i operations=10000
        df_filtered = df[(df['producers_consmers'] == prod_cons) & (df['operations'] == opers)]

        # Wyświetl dane w tabelce
        table = tabulate(
            df_filtered[
                ['max_portion', '3_locki Time', '3_locki CPU Time', '4_condition Time', '4_condition CPU Time']],
            headers='keys', tablefmt='pretty', showindex=False)

        print("Dane w tabelce:")
        print(table)

        # Utwórz wykresy dla 3_locki Time, 3_locki CPU Time, 4_condition Time i 4_condition CPU Time
        fig, ax = plt.subplots(figsize=(12, 8))

        # Wykres dla 3_locki Time
        ax.plot(df_filtered['max_portion'], df_filtered['3_locki Time'], label='3_locki Time')

        # Wykres dla 3_locki CPU Time
        # ax.plot(df_filtered['max_portion'], df_filtered['3_locki CPU Time'], label='3_locki CPU Time', linestyle='--')

        # Wykres dla 4_condition Time
        ax.plot(df_filtered['max_portion'], df_filtered['4_condition Time'], label='4_condition Time', color='green')

        # Wykres dla 4_condition CPU Time
        # ax.plot(df_filtered['max_portion'], df_filtered['4_condition CPU Time'], label='4_condition CPU Time',
        #         linestyle='--', color='red')

        # Ustawienia osi i tytułu
        ax.set_xlabel('Maksymalna porcja')
        ax.set_ylabel('Czas [ms]')
        title = "Czasy operacji od max_portion dla " + str(prod_cons) + " Producentów i konsumentów, " + str(
            opers) + " operations"
        ax.set_title(title)
        ax.legend()

        # Wyświetl wykres
        plt.show()
