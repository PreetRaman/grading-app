import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IFdaiNummer } from 'app/shared/model/fdai-nummer.model';
import { FdaiNummerService } from './fdai-nummer.service';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-fdai-nummer-update',
    templateUrl: './fdai-nummer-update.component.html'
})
export class FdaiNummerUpdateComponent implements OnInit {
    fdaiNummer: IFdaiNummer;
    isSaving: boolean;

    users: IUser[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private fdaiNummerService: FdaiNummerService,
        private userService: UserService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ fdaiNummer }) => {
            this.fdaiNummer = fdaiNummer;
        });
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.fdaiNummer.id !== undefined) {
            this.subscribeToSaveResponse(this.fdaiNummerService.update(this.fdaiNummer));
        } else {
            this.subscribeToSaveResponse(this.fdaiNummerService.create(this.fdaiNummer));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IFdaiNummer>>) {
        result.subscribe((res: HttpResponse<IFdaiNummer>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }
}
