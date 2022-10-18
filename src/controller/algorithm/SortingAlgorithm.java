package controller.algorithm;

import model.CellModel;

import java.util.List;

public class SortingAlgorithm {
    public CellModel sortCost(List<CellModel> data) {
        int n = data.size();
        if (n > 1) {
            for (int i = 1; i < n; i++) {
                if (data.get(i) != null) {
                    double current_Cost = data.get(i).getCost();
                    CellModel temp = data.get(i);
                    int j = i;
                    while (j > 0 && current_Cost < data.get(j - 1).getCost()) {
                        data.set(j, data.get(j - 1));
                        --j;
                    }
                    data.set(j, temp);
                }
            }
        }
        return data.get(0);

    }

    public CellModel sortFCost(List<CellModel> data) {
        int n = data.size();
        if (n > 1) {
            for (int i = 1; i < n; i++) {
                if (data.get(i) != null) {
                    double current_F_Cost = data.get(i).getF();
                    CellModel temp = data.get(i);
                    int j = i;
                    while (j > 0 && current_F_Cost < data.get(j - 1).getF()) {
                        data.set(j, data.get(j - 1));
                        --j;
                    }
                    data.set(j, temp);
                }
            }
        }
        return data.get(0);

    }
}
