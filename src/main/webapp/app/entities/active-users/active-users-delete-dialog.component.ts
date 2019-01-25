import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IActiveUsers } from 'app/shared/model/active-users.model';
import { ActiveUsersService } from './active-users.service';

@Component({
    selector: 'jhi-active-users-delete-dialog',
    templateUrl: './active-users-delete-dialog.component.html'
})
export class ActiveUsersDeleteDialogComponent {
    activeUsers: IActiveUsers;

    constructor(
        private activeUsersService: ActiveUsersService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.activeUsersService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'activeUsersListModification',
                content: 'Deleted an activeUsers'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-active-users-delete-popup',
    template: ''
})
export class ActiveUsersDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ activeUsers }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ActiveUsersDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.activeUsers = activeUsers;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
